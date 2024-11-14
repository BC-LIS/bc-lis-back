package com.bclis.service;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.persistence.entity.*;
import com.bclis.persistence.repository.*;
import com.bclis.utils.exceptions.NotFoundException;
import io.minio.*;
import lombok.RequiredArgsConstructor;
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

import java.io.InputStream;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DocumentFilterService documentFilterService;

    private final DocumentCategoryService documentCategoryService;

    private final MinioClient minioClient;
    private final ModelMapper modelMapper;

    @Value("${minio.bucket-name}")
    private String bucketName;

    // Método para crear un nuevo documento y subir el archivo a MinIO
    public DocumentResponseDTO createDocument(DocumentCreateDTO documentDTO) throws Exception {

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

        TypeEntity typeEntity = typeRepository.findByName(documentDTO.getTypeName())
                .orElseThrow(() -> new NotFoundException("Type not found"));

        UserEntity userEntity = userRepository.findByUsername(documentDTO.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));

        List<CategoryEntity> categoriesEntity = documentDTO.getCategories()
                .stream()
                .map(categoryString -> categoryRepository.findByName(categoryString)
                        .orElseThrow(() -> new NotFoundException("Category not found")))
                .toList();

        // Asignar el nombre del objeto en MinIO
        document.setObjectName(objectName);
        document.setType(typeEntity);
        document.setUser(userEntity);
        document.setCategories(categoriesEntity);

        // Guardar el documento en la base de datos
        DocumentEntity savedDocument = documentRepository.save(document);

        // Convertir la entidad guardada en DocumentResponseDTO usando ModelMapper
        return modelMapper.map(savedDocument, DocumentResponseDTO.class);
    }

    // Método para obtener información de un documento por ID
    public DocumentResponseDTO getDocumentById(Long id) {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

        // Crear el DTO de respuesta
        DocumentResponseDTO responseDTO = modelMapper.map(document, DocumentResponseDTO.class);

        return responseDTO;
    }

    public List<DocumentResponseDTO> getAllDocuments() {
        Specification<DocumentEntity> specification = documentFilterService.getDocumentsByType();
        List<DocumentEntity> documents = documentRepository.findAll(specification);

        return documents.stream()
                .map(document -> modelMapper.map(document, DocumentResponseDTO.class))
                .toList();
    }

    // Método para descargar un documento por ID
    public ResponseEntity<byte[]> downloadDocument(Long id) throws Exception {
        // Obtener la información del documento de la base de datos
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encontrado"));

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
            throw new RuntimeException("El archivo está vacío o no se pudo leer.");
        }

        // Obtener los metadatos del archivo en MinIO
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(document.getObjectName())
                        .build()
        );

        // Mostrar el tipo de contenido para depuración
        System.out.println("Tipo de contenido: " + stat.contentType());

        // Construir la respuesta con el archivo descargado
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(stat.contentType()))  // Usar el contentType de los metadatos
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getObjectName() + "\"")
                .body(fileBytes);
    }





}