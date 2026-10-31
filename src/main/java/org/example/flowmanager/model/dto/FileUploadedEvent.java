package org.example.flowmanager.model.dto;

public record FileUploadedEvent(String sourceKey, String resultKey, String status) {
}
