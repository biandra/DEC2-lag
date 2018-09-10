package com.decidir.domain;

import org.apache.kafka.common.TopicPartition;

/**
 * Created by biandra on 06/09/18.
 */
public class TopicPartitionDetail {
    private TopicPartition topic;
    private Long lastOffset;

    public TopicPartitionDetail(TopicPartition topic, Long lastOffset){
        this.topic = topic;
        this.lastOffset = lastOffset;
    }

    public TopicPartition getTopic() {
        return topic;
    }

    public void setTopic(TopicPartition topic) {
        this.topic = topic;
    }

    public Long getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(Long lastOffset) {
        this.lastOffset = lastOffset;
    }
}
