package org.example.flowmanager.service.mapper;

import org.example.flowmanager.model.FileEntity;
import org.example.flowmanager.model.dto.FileUploadedEvent;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {
    public FileUploadedEvent toDto(FileEntity entity) {
        return new FileUploadedEvent(
                entity.getSourceKey(),
                entity.getResultKey(),
                entity.getStatus()
        );
    }
}
