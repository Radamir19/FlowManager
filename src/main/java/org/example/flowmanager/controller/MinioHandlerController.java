package org.example.flowmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.flowmanager.model.dto.FileDownloadResponse;
import org.example.flowmanager.model.dto.FileUploadedEvent;
import org.example.flowmanager.service.FlowManagerService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/minio")
@Tag(name = "MinIO Storage", description = "Управление загрузкой файла в хранилище")
public class MinioHandlerController {
    private final FlowManagerService managerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка файла в MinIO хранилище")
    public ResponseEntity<FileUploadedEvent> uploadFileToMinio(@RequestParam MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(managerService.uploadFile(file));
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Просмотр текущего статуса конвертации")
    public ResponseEntity<String> getFileStatus(@PathVariable("id") UUID id) {
        return ResponseEntity.ok().body(managerService.getStatusById(id));
    }

    @GetMapping("/{id}/converted_file")
    @Operation(summary = "Получаем конвертированный файл")
    public ResponseEntity<Resource> getFile(@PathVariable("id") UUID id) {
        FileDownloadResponse fileResource = managerService.getFile(id);
        String contentDisposition = ContentDisposition.attachment()
                .filename(fileResource.fileOriginalName(), StandardCharsets.UTF_8)
                .build()
                .toString();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(fileResource.resource());
    }
}
