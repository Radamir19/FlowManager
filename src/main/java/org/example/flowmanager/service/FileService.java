package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.flowmanager.messaging.event.FileToConvertEvent;
import org.example.flowmanager.model.FileEntity;
import org.example.flowmanager.model.OutboxEntity;
import org.example.flowmanager.model.dto.FileUploadedEvent;
import org.example.flowmanager.model.status.FileStatus;
import org.example.flowmanager.model.status.OutboxStatus;
import org.example.flowmanager.repository.FileRepository;
import org.example.flowmanager.repository.OutboxRepository;
import org.example.flowmanager.service.mapper.FileMapper;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;
    private final FileMapper mapper;

    @Transactional
    public boolean updateStatus(String key, UUID fileId, String from, String to) {
        return fileRepository.updateStatus(key, fileId, from, to) == 1;
    }

    @Transactional
    public FileUploadedEvent registerUploadedFile(MultipartFile file, String bucket, String key) {
        OutboxEntity entity = new OutboxEntity();
        entity.setFileId(UUID.randomUUID());
        FileToConvertEvent event = new FileToConvertEvent(entity.getFileId(), bucket, key);
        entity.setAggregate(event.key());
        entity.setPayload(objectMapper.writeValueAsString(event));
        entity.setStatus(OutboxStatus.NEW.name());
        outboxRepository.save(entity);
        FileEntity fileEvent = new FileEntity();
        fileEvent.setId(entity.getFileId());
        fileEvent.setSourceKey(key);
        fileEvent.setStatus(FileStatus.NEW.name());
        FileUploadedEvent dto = mapper.toDto(fileEvent);
        fileRepository.save(fileEvent);
        return dto;
    }
}
