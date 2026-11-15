package org.example.flowmanager.model.dto;

import java.time.OffsetDateTime;

public record UserSubscription(String login, String subType, OffsetDateTime expireAt, Long maxFileSize) {
}
