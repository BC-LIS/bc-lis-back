package com.bclis.service;

import com.bclis.dto.request.DocumentCreateDTO;
import com.bclis.dto.response.DocumentResponseDTO;
import com.bclis.persistence.entity.DocumentEntity;
import com.bclis.persistence.repository.DocumentRepository;
import io.minio.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class DocumentService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ModelMapper modelMapper;

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

        // Asignar el nombre del objeto en MinIO
        document.setObjectName(objectName);

        // Guardar el documento en la base de datos
        DocumentEntity savedDocument = documentRepository.save(document);

        // Convertir la entidad guardada en DocumentResponseDTO usando ModelMapper
        return modelMapper.map(savedDocument, DocumentResponseDTO.class);
    }


    // Método para obtener información de un documento por ID
    public DocumentResponseDTO getDocumentById(Long id) {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Documento no encont    rado"));

        return modelMapper.map(document, DocumentResponseDTO.class);
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



    // Método para obtener información del documento y descargar el archivo en una sola llamada
    public ResponseEntity<byte[]> getDocumentWithInfo(Long id) throws Exception {
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

        // Obtener los metadatos del archivo en MinIO
        StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder()
                        .bucket(bucketName)
                        .object(document.getObjectName())
                        .build()
        );

        // Agregar los metadatos del documento en los headers de la respuesta
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(stat.contentType()));
        headers.setContentDispositionFormData("attachment", document.getObjectName());
        headers.add("Document-Id", document.getId().toString());
        headers.add("Document-Name", document.getName());
        headers.add("Document-Description", document.getDescription());

        // Devolver la respuesta con los metadatos en los headers y el archivo en el cuerpo
        return ResponseEntity.ok()
                .headers(headers)
                .body(fileBytes);
    }

}