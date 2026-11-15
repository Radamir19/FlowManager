package org.example.flowmanager.model.dto;

import org.springframework.core.io.Resource;

public record FileDownloadResponse(Resource resource, String fileOriginalName) {
}
