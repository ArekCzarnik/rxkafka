package com.infinity.rxkafka;

import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;
import org.junit.Assert;
import org.junit.Test;

public class ProducerTest {

    @Test
    public void testProducer() {
        final KafkaEventProducer eventProducer = new KafkaEventProducer("console");
        for (int i = 0; i < 100; i++) {
            eventProducer.publish("test"+i);
        }
    }

    @Test
    public void testParallel() {
        Flowable.range(1, 10)
                .parallel()
                .runOn(Schedulers.computation())
                .map(v -> v * v)
                .sequential().takeLast(1)
                .blockingSubscribe(integer -> Assert.assertTrue(integer == 100));
    }
}
