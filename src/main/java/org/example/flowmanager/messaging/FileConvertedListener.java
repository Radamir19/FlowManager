package org.example.flowmanager.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.messaging.event.FileConvertedEvent;
import org.example.flowmanager.model.status.InboxStatus;
import org.example.flowmanager.service.InboxService;
import org.example.flowmanager.service.util.FileWorker;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FileConvertedListener {
    private final InboxService inboxService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.converted_topic}")
    public void onMessage(String message) throws Exception {
        FileConvertedEvent event = objectMapper.readValue(message, FileConvertedEvent.class);
        log.info("Файл {} успешно конвертировался!", FileWorker.getFileName(event.sourceKey()));
        inboxService.claim(FileWorker.getEventId(event.resultKey()), List.of(InboxStatus.RECEIVED.name()), InboxStatus.PROCESSED.name());
    }
}
