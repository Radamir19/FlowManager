package org.example.flowmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "inbox")
public class InboxEntity {
    @Id
    private UUID eventId;
    private OffsetDateTime processedAt;

    @Column(nullable = false)
    private String status;
}
