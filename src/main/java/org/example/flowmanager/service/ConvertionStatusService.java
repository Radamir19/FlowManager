package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConvertionStatusService {
    private final InboxService inboxService;
    private final FileService fileService;

    @Transactional
    public void updateStatusForFileAndInbox(String key, UUID eventId, String fileStatusFrom, String fileStatusTo, List<String> inboxStatusFrom, String inboxStatusTo) {
        inboxService.claim(eventId, inboxStatusFrom,inboxStatusTo);
        fileService.updateStatus(key, eventId, fileStatusFrom, fileStatusTo);
    }
}
