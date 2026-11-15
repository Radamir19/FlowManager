package org.example.flowmanager.messaging.event;

public record FileConvertedEvent(String sourceKey, String resultKey, String bucket) {
}
