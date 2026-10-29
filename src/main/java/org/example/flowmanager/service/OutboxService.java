package org.example.flowmanager.service;

import lombok.RequiredArgsConstructor;
import org.example.flowmanager.model.OutboxEntity;
import org.example.flowmanager.repository.OutboxRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OutboxService {
    private final OutboxRepository outboxRepository;

    @Transactional
    public List<OutboxEntity> claimBatch(int limit) {
        return outboxRepository.claimBatch(limit);
    }

    @Transactional
    public void finishBatch(List<UUID> sendIds, List<UUID> failedIds, Short maxAttempts) {
        outboxRepository.markSent(sendIds);
        outboxRepository.markFailed(failedIds, maxAttempts);
    }
}
