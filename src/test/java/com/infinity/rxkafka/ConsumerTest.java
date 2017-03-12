package com.infinity.rxkafka;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class ConsumerTest {

    @Test
    public void testConsumer() {
        KafkaEventConsumer kafkaEventConsumer = new KafkaEventConsumer("console");
        kafkaEventConsumer.consume().take(10, TimeUnit.SECONDS).blockingForEach(s -> {
            System.out.println(s);
        });
        kafkaEventConsumer.complete();
    }
}
