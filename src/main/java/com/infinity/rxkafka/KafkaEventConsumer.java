package com.infinity.rxkafka;

import io.reactivex.Flowable;
import io.reactivex.processors.FlowableProcessor;
import io.reactivex.processors.PublishProcessor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;


public class KafkaEventConsumer implements EventConsumer {

    private final String topic;
    private final FlowableProcessor<String> subject;
    private final Properties properties;
    private final KStreamBuilder builder;
    private final KStream<byte[], String> stream;
    private final KafkaStreams streams;

    public KafkaEventConsumer(String topic) {
        this.topic = topic;
        properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG,
                "rxkafka-streams");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        properties.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG,
                Serdes.ByteArray().getClass().getName());
        properties.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,
                Serdes.String().getClass().getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        subject = PublishProcessor.create();
        builder = new KStreamBuilder();
        stream = builder.stream(topic);
        stream.foreach((key, value) -> {
            subject.onNext(value);
        });
        streams = new KafkaStreams(builder, properties);
        streams.start();
    }

    @Override
    public Flowable<String> consume() {
        return subject;
    }

    public void complete() {
        streams.cleanUp();
        subject.onComplete();
        streams.close();
    }

    public static void main(String[] args) {
        KafkaEventConsumer kafkaEventConsumer = new KafkaEventConsumer("console");
        kafkaEventConsumer.consume().subscribe(string -> System.out.println(string));
    }
}
