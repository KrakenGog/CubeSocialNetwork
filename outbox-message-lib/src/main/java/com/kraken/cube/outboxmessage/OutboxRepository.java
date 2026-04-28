package com.kraken.cube.outboxmessage;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {
    @Query(value = """
        SELECT * FROM outbox_events 
        WHERE processed = false 
        ORDER BY created_at ASC 
        LIMIT :n 
        FOR UPDATE SKIP LOCKED
        """, nativeQuery = true)
    public List<OutboxMessage> getFirstNMessage(@Param("n") int n);
}
