package org.example.flowmanager.messaging.event;

import java.util.UUID;

public record FileToConvertEvent(UUID eventId, String bucket, String key) {
}
