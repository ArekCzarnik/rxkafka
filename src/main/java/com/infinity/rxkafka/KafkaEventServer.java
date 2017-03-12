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


public class KafkaEventServer implements EventConsumer {

    private final FlowableProcessor<String> subject;
    private Properties config = new Properties();
    private KStreamBuilder builder;
    private KStream<byte[], String> stream;
    private KafkaStreams streams;

    public KafkaEventServer() {
        config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG,
                "exclamation-kafka-streams");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        config.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "localhost:2181");
        config.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG,
                Serdes.ByteArray().getClass().getName());
        config.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG,
                Serdes.String().getClass().getName());


        subject = PublishProcessor.create();
        builder = new KStreamBuilder();
        stream = builder.stream("console");
        stream.foreach((key, value) -> {
            subject.onNext(value);
        });
        streams = new KafkaStreams(builder, config);
        streams.start();
    }

    @Override
    public Flowable<String> consume() {
        return subject;
    }

    public void complete() {
        subject.onComplete();
        streams.close();
    }

    public static void main(String[] args) {
        KafkaEventServer kafkaEventServer = new KafkaEventServer();
        kafkaEventServer.consume().subscribe(string -> {
            System.out.println(string);
        });
    }
}
