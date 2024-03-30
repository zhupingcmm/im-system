package com.ocbc.im.cdec;

import com.alibaba.fastjson.JSONObject;
import com.ocbc.im.cdec.proto.MessagePack;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<MessagePack<Object>> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessagePack<Object> messagePack, ByteBuf byteBuf) throws Exception {
        String s = JSONObject.toJSONString(messagePack.getData());
        byte [] bytes = s.getBytes();
        byteBuf.writeInt(messagePack.getCommand());
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
