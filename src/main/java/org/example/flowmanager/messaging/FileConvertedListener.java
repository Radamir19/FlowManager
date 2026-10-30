package org.example.flowmanager.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.messaging.event.FileConvertedEvent;
import org.example.flowmanager.model.FileEntity;
import org.example.flowmanager.model.status.FileStatus;
import org.example.flowmanager.model.status.InboxStatus;
import org.example.flowmanager.repository.FileRepository;
import org.example.flowmanager.service.FileService;
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
    private final FileService fileService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.converted_topic}")
    public void onMessage(String message) throws Exception {
        FileConvertedEvent event = objectMapper.readValue(message, FileConvertedEvent.class);
        log.info("Файл {} успешно конвертировался!", FileWorker.getFileName(event.sourceKey()));
        fileService.updateStatus(event.resultKey(), FileWorker.getEventId(event.resultKey()), FileStatus.NEW.name(), FileStatus.CONVERTED.name());
        inboxService.claim(FileWorker.getEventId(event.resultKey()), List.of(InboxStatus.RECEIVED.name()), InboxStatus.PROCESSED.name());
    }
}
