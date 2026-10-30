package org.example.flowmanager.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.messaging.event.FileConvertedEvent;
import org.example.flowmanager.messaging.event.FileToConvertEvent;
import org.example.flowmanager.model.status.FileStatus;
import org.example.flowmanager.model.status.InboxStatus;
import org.example.flowmanager.service.FileService;
import org.example.flowmanager.service.InboxService;
import org.example.flowmanager.service.util.FileWorker;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileFailedListener {
    private final InboxService inboxService;
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.failed_topic}")
    public void onMessage(String message) {
        JsonNode node = objectMapper.readTree(message);
        if (node.isTextual()) {
            node = objectMapper.readTree(node.asText());
        }
        FileToConvertEvent event = objectMapper.treeToValue(node, FileToConvertEvent.class);
        log.warn("Файл {} не удалось конвертировать, попробуйте ещё раз(принимаем только .txt/.png/.jpg/.zip)", FileWorker.getFileName(event.key()));
        fileService.updateStatus(null, event.eventId(), FileStatus.NEW.name(), FileStatus.ERROR.name());
        inboxService.claim(event.eventId(), List.of(InboxStatus.RECEIVED.name()), InboxStatus.ERROR.name());
    }
}
