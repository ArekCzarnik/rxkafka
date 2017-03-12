package com.infinity.rxkafka;

import org.junit.Assert;
import org.junit.Test;

public class ConsumerTest {

    @Test
    public void testSender() {
        KafkaEventServer kafkaEventServer = new KafkaEventServer();
        kafkaEventServer.consume().take(1).subscribe(string -> {
            System.out.println(string);
            kafkaEventServer.complete();
            Assert.assertTrue(true);
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
