package com.infinity.rxkafka;

import io.reactivex.Observable;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class KafkaEventProducer implements EventProducer<String> {

    private final Properties properties;
    private final String topic;
    private final ObservableQueue<String> observableQueue;
    private final KafkaProducer kafkaProducer;
    private final Observable<String> stream;


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

        kafkaProducer = new KafkaProducer<>(properties);
        this.topic = topic;

        this.observableQueue = new ObservableQueue<>();
        this.stream = observableQueue.observe();

        this.stream.subscribe(value -> {
            kafkaProducer.send(new ProducerRecord<>(topic, value));
            kafkaProducer.flush();
        });
    }

    @Override
    public void publish(final String message) {
        observableQueue.add(message);
    }
}
