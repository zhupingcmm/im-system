package com.ocbc.im.cdec.utils;

import com.alibaba.fastjson.JSONObject;
import com.ocbc.im.cdec.proto.Message;
import com.ocbc.im.cdec.proto.MessageHeader;
import com.ocbc.im.common.enums.command.MessageCommand;
import io.netty.buffer.ByteBuf;

/**
 * @author: Ray
 * @description: 将ByteBuf转化为Message实体，根据私有协议转换
 *               私有协议规则，
 *               4位表示Command表示消息的开始，
 *               4位表示version
 *               4位表示clientType
 *               4位表示messageType
 *               4位表示appId
 *               4位表示imei长度
 *               imei
 *               4位表示数据长度
 *               data
 *               后续将解码方式加到数据头根据不同的解码方式解码，如pb，json，现在用json字符串
 * @version: 1.0
 */
public class ByteBufToMessageUtils {

    public static Message transferToMessage(ByteBuf byteBuf) {
        /** 获取command*/
        int command = byteBuf.readInt();

        /** 获取version*/
        int version = byteBuf.readInt();

        /** 获取clientType*/
        int clientType = byteBuf.readInt();

        /** 获取clientType*/
        int messageType = byteBuf.readInt();

        /** 获取appId*/
        int appId = byteBuf.readInt();

        /** 获取imeiLength*/
        int imeiLength = byteBuf.readInt();

        /** 获取bodyLen*/
        int bodyLen = byteBuf.readInt();


        if (byteBuf.readableBytes() < bodyLen + imeiLength) {
            byteBuf.resetReaderIndex();
            return null;
        }


        byte[] imeiData = new byte[imeiLength];
        byteBuf.readBytes(imeiData);
        String imei = new String(imeiData);


        byte[] bodyData = new byte[bodyLen];
        byteBuf.readBytes(bodyData);


        MessageHeader messageHeader = new MessageHeader();
        messageHeader.setAppId(appId);
        messageHeader.setClientType(clientType);
        messageHeader.setCommand(command);
        messageHeader.setLength(bodyLen);
        messageHeader.setVersion(version);
        messageHeader.setMessageType(messageType);
        messageHeader.setImei(imei);



        Message message = new Message();

        message.setMessageHeader(messageHeader);

        if (messageType == MessageCommand.JSON.getCommand()) {
            String body = new String(bodyData);
            JSONObject parse = (JSONObject) JSONObject.parse(body);
            message.setMessagePack(parse);
        }

        return message;
    }

}
