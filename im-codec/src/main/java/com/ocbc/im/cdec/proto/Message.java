package com.ocbc.im.cdec.proto;

import lombok.Data;

@Data
public class Message {
    private MessageHeader messageHeader;
    private Object messagePack;

    @Override
    public String toString() {
        return "Message{" +
                "messageHeader=" + messageHeader +
                ", messagePack=" + messagePack +
                '}';
    }
}
