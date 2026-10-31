package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.exception.FileConvertionFailedException;
import org.example.flowmanager.exception.FileNotFoundException;
import org.example.flowmanager.model.FileEntity;
import org.example.flowmanager.model.dto.FileDownloadResponse;
import org.example.flowmanager.model.dto.FileUploadedEvent;
import org.example.flowmanager.repository.FileRepository;
import org.example.flowmanager.storage.MinioStorage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlowManagerService {
    private final FileRepository fileRepository;
    private final FileService fileService;
    private final MinioStorage worker;

    @Value("${minio.bucket}")
    private String bucket;
    @Value("${minio.key}")
    private String key;


    public FileUploadedEvent uploadFile(MultipartFile file) {
        String sourceKey = key + "/" + file.getOriginalFilename();
        FileUploadedEvent event = null;
        try {
            worker.upload(file, bucket, sourceKey);
            event = fileService.registerUploadedFile(file, bucket, sourceKey);
        } catch (Exception e) {
            log.warn("Не удалось загрузить в MinIO: {}", e.getMessage());
        }
        return event;
    }

    public String getStatusById(UUID fileId) {
        FileEntity entity = fileRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("Файл по такому ID не найден."));
        return entity.getStatus();
    }

    public FileDownloadResponse getFile(UUID fileId) {
        FileEntity entity = fileRepository.findByIdWhereStatusIsConverted(fileId);
        if (entity == null) {
            throw new FileNotFoundException("Сконвертированный файл не найден или еще не готов");
        }

        try {
            InputStream stream = worker.download(entity.getResultKey(), bucket);
            InputStreamResource resource = new InputStreamResource(stream);

            String originalName = entity.getSourceKey();
            String pdfName = originalName.contains(".")
                    ? originalName.substring(0, originalName.lastIndexOf(".")) + ".pdf"
                    : originalName + ".pdf";
            return new FileDownloadResponse(resource, pdfName);
        } catch (Exception e) {
            throw new FileConvertionFailedException("Ошибка при скачивании файла");
        }
    }

}
