package com.infinity.rxkafka.configuration;


import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Properties;

public class ConsumerConnectorFactory {

    private String zookeeper;
    private String groupId;

    public KafkaConsumer create() {
        Properties props = new Properties();
        props.put("zookeeper.connect", zookeeper);
        props.put("group.id", groupId);
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        return new KafkaConsumer(props, new StringDeserializer(), new StringDeserializer());
    }
}
