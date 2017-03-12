package com.infinity.rxkafka;

import org.junit.Assert;
import org.junit.Test;

public class ConsumerTest {

    @Test
    public void testConsumer() {
        KafkaEventConsumer kafkaEventConsumer = new KafkaEventConsumer("console");
        kafkaEventConsumer.consume().takeLast(1).subscribe(string -> {
            System.out.println(string);
            kafkaEventConsumer.complete();
            Assert.assertTrue(true);
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
