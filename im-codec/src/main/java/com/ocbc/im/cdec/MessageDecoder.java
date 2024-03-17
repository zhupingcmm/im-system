package com.ocbc.im.cdec;

import com.ocbc.im.cdec.proto.Message;
import com.ocbc.im.cdec.utils.ByteBufToMessageUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf byteBuf, List<Object> list) throws Exception {

        //请求头（指令
        // 版本
        // clientType
        // 消息解析类型
        // appId
        // imei长度
        // bodylen）+ imei号 + 请求体

        if (byteBuf.readableBytes() < 28) return;
        Message message = ByteBufToMessageUtils.transferToMessage(byteBuf);
        if (message == null) return;

        list.add(message);
    }
}
