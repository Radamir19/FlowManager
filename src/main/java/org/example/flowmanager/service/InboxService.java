package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.flowmanager.repository.InboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InboxService {
    private final InboxRepository inboxRepository;

    @Transactional
    public boolean claim(UUID eventId, List<String> from, String to) {
        inboxRepository.tryInsert(eventId);
        return inboxRepository.updateStatus(eventId, from, to) == 1;
    }

}
