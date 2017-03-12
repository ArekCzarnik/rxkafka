package com.infinity.rxkafka;

import org.junit.Test;

public class ProducerTest {

    @Test
    public void testProducer() {
        final KafkaEventProducer eventProducer = new KafkaEventProducer("console");
        eventProducer.publish("test");
    }
}
