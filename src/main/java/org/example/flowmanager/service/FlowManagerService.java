package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.messaging.event.FileToConvertEvent;
import org.example.flowmanager.model.OutboxEntity;
import org.example.flowmanager.model.status.OutboxStatus;
import org.example.flowmanager.repository.OutboxRepository;
import org.example.flowmanager.service.util.FileWorker;
import org.example.flowmanager.storage.MinioStorage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlowManagerService {
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final MinioStorage worker;

    @Transactional
    public void downloadFile(MultipartFile file, String bucket, String key) {
        try {
            worker.upload(file, bucket, key);
        } catch (Exception e) {
            log.warn("Не удалось загрузить в MinIO: {}", e.getMessage());
        }
        OutboxEntity entity = new OutboxEntity();
        entity.setFileId(UUID.randomUUID());
        FileToConvertEvent event = new FileToConvertEvent(entity.getFileId(), bucket, key);
        entity.setAggregate(event.key());
        entity.setPayload(objectMapper.writeValueAsString(event));
        entity.setStatus(OutboxStatus.NEW.name());
        outboxRepository.save(entity);
    }

}
