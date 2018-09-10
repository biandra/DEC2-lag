package com.decidir;

import com.decidir.domain.PartionOffsets;
import com.decidir.domain.TopicPartitionDetail;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Created by biandra on 05/09/18.
 */
public class KafkaConsumerMonitor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerMonitor.class);

    private final String monitoringConsumerGroupID = "monitoring_consumer_" + UUID.randomUUID().toString();

   /* public Map<TopicPartition, PartionOffsets> getConsumerGroupOffsets(String host, String topic, String groupId) {
        Map<TopicPartition, Long> logEndOffset = getLogEndOffset(topic, host);


        KafkaConsumer consumer = createNewConsumer(groupId, host);

        BinaryOperator<PartionOffsets> mergeFunction = (a, b) -> {
            throw new IllegalStateException();
        };

        Map<TopicPartition, PartionOffsets> result = logEndOffset.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> (entry.getKey()),
                        entry -> {
                            OffsetAndMetadata committed = consumer.committed(entry.getKey());
                            return new PartionOffsets(entry.getValue(), committed.offset(), entry.getKey().partition(), topic);
                        }, mergeFunction));


        return result;
    }*/

    public List<TopicPartitionDetail> getLogEndOffset(String topic, String host) {
        List<TopicPartitionDetail> endOffsets = new ArrayList<>();
        KafkaConsumer<?, ?> consumer = createNewConsumer(monitoringConsumerGroupID, host);
        List<PartitionInfo> partitionInfoList = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = partitionInfoList.stream().map(pi -> new TopicPartition(topic, pi.partition())).collect(Collectors.toList());
        consumer.assign(topicPartitions);
        consumer.seekToEnd(topicPartitions);
        topicPartitions.forEach(topicPartition -> {
            Long lastOffset = consumer.position(topicPartition);
            endOffsets.add(new TopicPartitionDetail(topicPartition, lastOffset));
            LOGGER.info("topic {} - partition {} - lastOffset {}", topicPartition.topic(), topicPartition.partition(), lastOffset);
        });
        consumer.close();
        return endOffsets;
    }

    private static KafkaConsumer<?, ?> createNewConsumer(String groupId, String host) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, host);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return new KafkaConsumer<>(properties);
    }
}
