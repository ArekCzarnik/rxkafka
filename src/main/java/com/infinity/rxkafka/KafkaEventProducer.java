package com.infinity.rxkafka;

import io.reactivex.Observable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaEventProducer implements EventProducer<String> {

    private final Properties properties;
    private final String topic;
    private final FlowableProcessor<String> subject;
    private final KafkaProducer kafkaProducer;

    public KafkaEventProducer(String topic) {
        properties = new Properties();
        properties.put("client.id",
                "rxkafka-streams-producer");
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("batch.size", 16384);
        properties.put("linger.ms", 1);
        properties.put("buffer.memory", 33554432);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        subject = PublishProcessor.create();

        kafkaProducer = new KafkaProducer<>(properties);
        this.topic = topic;

        subject.subscribe(value -> {
            kafkaProducer.send(new ProducerRecord<>(topic, value));
            kafkaProducer.flush();
        });
    }

    @Override
    public void publish(final String message) {
        subject.onNext(message);
    }
}
