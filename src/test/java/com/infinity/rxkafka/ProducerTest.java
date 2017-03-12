package com.infinity.rxkafka;

import org.junit.Test;

public class ProducerTest {

    @Test
    public void testProducer() {
        final KafkaEventProducer eventProducer = new KafkaEventProducer("console");
        for (int i = 0; i < 100; i++) {
            eventProducer.publish("test"+i);
        }
    }
}
