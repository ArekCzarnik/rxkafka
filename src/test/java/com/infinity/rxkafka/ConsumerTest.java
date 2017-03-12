package com.infinity.rxkafka;

import org.junit.Assert;
import org.junit.Test;

public class ConsumerTest {

    @Test
    public void testSender() {
        KafkaEventServer kafkaEventServer = new KafkaEventServer();
        kafkaEventServer.consume().take(1).subscribe(string -> {
            kafkaEventServer.complete();
            Assert.assertTrue(true);
        });
    }
}
