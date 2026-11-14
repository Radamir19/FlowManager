package org.example.flowmanager.messaging.event;

import java.time.OffsetDateTime;

public record UserSubscriptionEvent(String login, String subType, OffsetDateTime expireAt) {
}
