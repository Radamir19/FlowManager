package org.example.flowmanager.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "file")
@Getter
@Setter
@NoArgsConstructor
public class FileEntity {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String sourceKey;
    private String resultKey;

    @Column(nullable = false)
    private String status;
}
