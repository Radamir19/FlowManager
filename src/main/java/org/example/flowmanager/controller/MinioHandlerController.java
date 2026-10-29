package org.example.flowmanager.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.flowmanager.service.FlowManagerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/minio")
@Tag(name = "MinIO Storage", description = "Управление загрузкой файла в хранилище")
public class MinioHandlerController {
    private final FlowManagerService managerService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузка файла в MinIO хранилище")
    public ResponseEntity<Void> downloadFileToMinio(@RequestParam MultipartFile file,
                                                    @RequestParam String bucket,
                                                    @RequestParam String key) {
        managerService.downloadFile(file, bucket, key);
        return ResponseEntity.noContent().build();
    }
}
