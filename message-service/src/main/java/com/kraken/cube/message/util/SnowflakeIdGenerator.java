package com.kraken.cube.message.util;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGenerator {
    static final long EPOCH = 1704067200000L;
    static final long WORKER_ID_BITS = 10L;
    static final long SEQUENCE_BITS = 12L;

    static final long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
    static final long TIMESTAMP_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    static final long SEQUENCE_MASK = ~(-1 << SEQUENCE_BITS);


    private long workerId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;


    public SnowflakeIdGenerator(@Value("${app.snowflake.worker-id:1}") long workerId) {
        
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(
                String.format("Fatal error! Worker ID cant be greater than %d or less than 0. Current: %d", 
                              MAX_WORKER_ID, workerId)
            );
        }
        
        this.workerId = workerId;
    }

    public synchronized long nextId(){
        long timestamp = System.currentTimeMillis();

        if(timestamp < lastTimestamp)
            throw new RuntimeException("Illegal state. Current time is less than previous");

        if(timestamp == lastTimestamp){
            sequence = (sequence + 1L) & SEQUENCE_MASK;

            if(sequence == 0L){
                timestamp = waitForNextMillisecond();
            }
        }
        else{
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - EPOCH) << TIMESTAMP_SHIFT) | (workerId << WORKER_ID_SHIFT) | sequence;
    }

    private long waitForNextMillisecond(){
        while(true){
            if(lastTimestamp != System.currentTimeMillis())
                return System.currentTimeMillis();
        }
    }
}
