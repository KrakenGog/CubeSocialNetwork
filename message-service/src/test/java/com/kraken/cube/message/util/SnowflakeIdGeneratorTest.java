package com.kraken.cube.message.util;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.Assertions.assertThat;

public class SnowflakeIdGeneratorTest {

    @Test
    public void shouldGenerateValidId_whenNextIdCalled() {
        long workerId = 1L;
        SnowflakeIdGenerator generator = new SnowflakeIdGenerator(workerId);
        long timeBefore = System.currentTimeMillis() - SnowflakeIdGenerator.EPOCH;
        long id = generator.nextId();
        long timeAfter = System.currentTimeMillis() - SnowflakeIdGenerator.EPOCH;

        assertThat((id >> (SnowflakeIdGenerator.WORKER_ID_BITS + SnowflakeIdGenerator.SEQUENCE_BITS)))
                .isBetween(timeBefore, timeAfter);

        assertThat((id >> (SnowflakeIdGenerator.WORKER_ID_SHIFT)) & SnowflakeIdGenerator.MAX_WORKER_ID)
                .isEqualTo(workerId);

        assertThat(id & SnowflakeIdGenerator.SEQUENCE_BITS).isEqualTo(0);
    }
}
