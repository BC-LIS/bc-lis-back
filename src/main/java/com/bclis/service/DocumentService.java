package com.bclis.service;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.request.DocumentUpdateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.persistence.entity.*;
import com.bclis.persistence.repository.*;
import com.bclis.utils.exceptions.FileProcessingException;
import com.bclis.utils.exceptions.NotFoundException;
import io.minio.*;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DocumentFilterService documentFilterService;

    private final MinioClient minioClient;
    private final ModelMapper modelMapper;

    @Value("${minio.bucket-name}")
    private String bucketName;

    private static final String DOCUMENT_NOT_FOUND = "Document not found";

    // Método para crear un nuevo documento y subir el archivo a MinIO
    public DocumentResponseDTO createDocument(DocumentCreateDTO documentDTO) throws MinioException, IOException, GeneralSecurityException {

        // Obtener el archivo del DTO
        MultipartFile file = documentDTO.getFile();

        // Generar un nombre único para el objeto en MinIO
        String objectName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        // Subir el archivo a MinIO
        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build()
        );

        // Convertir el DTO en la entidad Document usando ModelMapper
        DocumentEntity document = modelMapper.map(documentDTO, DocumentEntity.class);

        // Obtener el tipo y usuario desde los repositorios
        TypeEntity typeEntity = typeRepository.findByName(documentDTO.getTypeName())
                .orElseThrow(() -> new NotFoundException("Type not found"));

        UserEntity userEntity = userRepository.findByUsername(documentDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        // Obtener las categorías desde el repositorio
        documentDTO.getCategories()
                .stream()
                .map(categoryString -> categoryRepository.findByName(categoryString)
                        .orElseThrow(() -> new NotFoundException("Category not found")))
                .forEach(document::addCategory); // Usamos el método addCategory

        // Asignar el nombre del objeto en MinIO, tipo y usuario
        document.setObjectName(objectName);
        document.setType(typeEntity);
        document.setUser(userEntity);

        // Guardar el documento en la base de datos
        DocumentEntity savedDocument = documentRepository.save(document);

        // Convertir la entidad guardada en DocumentResponseDTO usando ModelMapper
        return modelMapper.map(savedDocument, DocumentResponseDTO.class);
    }

    // Método para obtener información de un documento por ID
    public DocumentResponseDTO getDocumentById(Long id) {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));

        return modelMapper.map(document, DocumentResponseDTO.class);
    }

    public List<DocumentResponseDTO> getAllDocuments() {
        Specification<DocumentEntity> specification = documentFilterService.getDocumentsByType();
        List<DocumentEntity> documents = documentRepository.findAll(specification);

        return documents.stream()
                .map(document -> modelMapper.map(document, DocumentResponseDTO.class))
                .toList();
    }

    // Método para descargar un documento por ID
    public ResponseEntity<byte[]> downloadDocument(Long id) throws MinioException, IOException, GeneralSecurityException {
        // Obtener la información del documento de la base de datos
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));

        // Obtener el archivo desde MinIO usando el objectName
        InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(document.getObjectName())
                        .build()
        );

        // Leer el archivo como un array de bytes
        byte[] fileBytes = stream.readAllBytes();
        stream.close();

        // Verificar si el archivo se ha leído correctamente
        if (fileBytes.length == 0) {
            throw new FileProcessingException("El archivo está vacío o no se pudo leer.");
        }

        // Obtener los metadatos del archivo en MinIO
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(document.getObjectName())
                        .build()
        );

        // Mostrar el tipo de contenido para depuración
        log.info("Tipo de contenido: {}", stat.contentType());

        // Construir la respuesta con el archivo descargado
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(stat.contentType()))  // Usar el contentType de los metadatos
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getObjectName() + "\"")
                .body(fileBytes);
    }

    // Método para eliminar un documento por ID
    public void deleteDocument(Long id) throws MinioException, IOException, GeneralSecurityException {
        // Obtener el documento desde la base de datos
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));

        // Eliminar el archivo de MinIO
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(document.getObjectName())
                        .build()
        );

        // Eliminar el documento de la base de datos
        documentRepository.delete(document);
    }

    // Método para actualizar un documento
    public DocumentResponseDTO updateDocument(Long documentId, DocumentUpdateDTO documentDTO) throws NotFoundException {

        // Obtener el documento existente desde la base de datos
        DocumentEntity document = documentRepository.findById(documentId)
                .orElseThrow(() -> new NotFoundException(DOCUMENT_NOT_FOUND));

        // Actualizar el nombre
        if (documentDTO.getName() != null && !documentDTO.getName().isEmpty()) {
            document.setName(documentDTO.getName());
        }

        // Actualizar la descripción
        if (documentDTO.getDescription() != null && !documentDTO.getDescription().isEmpty()) {
            document.setDescription(documentDTO.getDescription());
        }

        // Actualizar el estado
        if (documentDTO.getState() != null) {
            document.setState(documentDTO.getState());
        }

        // Actualizar las categorías
        if (documentDTO.getCategories() != null && !documentDTO.getCategories().isEmpty()) {
            List<CategoryEntity> categoriesEntity = documentDTO.getCategories()
                    .stream()
                    .map(categoryString -> categoryRepository.findByName(categoryString)
                            .orElseThrow(() -> new NotFoundException("Category not found")))
                    .toList();

            // Limpiar y actualizar las categorías usando métodos de conveniencia
            document.getCategories().clear();
            categoriesEntity.forEach(document::addCategory);
        }

        // Guardar el documento actualizado
        DocumentEntity updatedDocument = documentRepository.save(document);

        // Convertir la entidad document actualizada a DocumentResponseDTO usando ModelMapper
        return modelMapper.map(updatedDocument, DocumentResponseDTO.class);
    }

}