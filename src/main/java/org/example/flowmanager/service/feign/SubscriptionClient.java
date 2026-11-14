package org.example.flowmanager.service.feign;

import org.example.flowmanager.model.dto.UserSubscription;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "subscription-service")
public interface SubscriptionClient {

    @Cacheable(cacheNames = "subscriptions", key = "#login")
    @GetMapping("api/v1/subscription/{id}")
    UserSubscription getSubscription(@PathVariable("id") String login);
}
