package org.example.flowmanager.messaging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.flowmanager.messaging.event.UserSubscriptionEvent;
import org.example.flowmanager.service.FlowManagerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class SubscriptionExpiredListener {
    private final FlowManagerService managerService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${app.kafka.topics.sub_expired}")
    public void onMessage(String message) {
        UserSubscriptionEvent event = objectMapper.readValue(message, UserSubscriptionEvent.class);
        log.info("Платная подписка у клиента с логином {} закончилась", event.login());
        managerService.evictSubscription(event.login());
    }
}
