package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.messaging.event.FileToConvertEvent;
import org.example.flowmanager.model.OutboxEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxRelay {
    private final OutboxService outboxService;
    private final KafkaTemplate<String, FileToConvertEvent> template;
    private final ObjectMapper objectMapper;

    @Value("${app.kafka.topics.to_convert}")
    private String toConvertTopic;
    @Value("${app.outbox.max-attempts}")
    private Short maxAttempts;

    @Scheduled(fixedDelay = 1000)
    public void publishPending() {
        List<OutboxEntity> batch = outboxService.claimBatch(10);
        List<UUID> sendIds = new ArrayList<>();
        List<UUID> failedIds = new ArrayList<>();
        Map<UUID, CompletableFuture<?>> pending = new HashMap<>();
        for(OutboxEntity outbox : batch) {
            try {
                FileToConvertEvent event = objectMapper.readValue(outbox.getPayload(), FileToConvertEvent.class);
                log.info("Событие {} отправилось", event);
                pending.put(outbox.getFileId(), template.send(toConvertTopic, outbox.getAggregate(), event));
            } catch(Exception e) {
                failedIds.add(outbox.getFileId());
                log.warn("Не удалось отправить {}", outbox.getFileId(), e);
            }
        }
        for(Map.Entry<UUID, CompletableFuture<?>> entry : pending.entrySet()) {
            try {
                entry.getValue().get(10, TimeUnit.SECONDS);
                sendIds.add(entry.getKey());
            } catch(Exception e) {
                failedIds.add(entry.getKey());
                log.warn("Событие {} не отправлено, повтор на следующем тике", entry.getKey(), e);
            }
        }
        outboxService.finishBatch(sendIds, failedIds, maxAttempts);
    }
}
