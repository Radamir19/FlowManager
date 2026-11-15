package org.example.flowmanager.repository;

import org.example.flowmanager.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, UUID> {
    @Query(value = "SELECT * FROM file WHERE id = :id AND status = 'CONVERTED'", nativeQuery = true)
    FileEntity findByIdWhereStatusIsConverted(@Param("id")UUID fileId);

    @Modifying
    @Query(value = "UPDATE file SET result_key = CASE WHEN :to = 'CONVERTED'THEN :key ELSE null END, status = :to WHERE id = :id AND status = :from",
            nativeQuery = true)
    int updateStatus(@Param("key") String key, @Param("id") UUID fileId, @Param("from") String from, @Param("to") String to);
}
