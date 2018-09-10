package com.decidir.domain;

/**
 * Created by biandra on 06/09/18.
 */
public class PartionOffsets {
    private long endOffset;
    private long currentOffset;
    private int partion;
    private String topic;

    public PartionOffsets(long endOffset, long currentOffset, int partion, String topic) {
        this.endOffset = endOffset;
        this.currentOffset = currentOffset;
        this.partion = partion;
        this.topic = topic;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public long getCurrentOffset() {
        return currentOffset;
    }

    public int getPartion() {
        return partion;
    }

    public String getTopic() {
        return topic;
    }
}
