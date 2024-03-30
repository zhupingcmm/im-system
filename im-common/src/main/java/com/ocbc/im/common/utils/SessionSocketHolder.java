package com.ocbc.im.common.utils;

import com.ocbc.im.common.model.UserClientDto;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionSocketHolder {

    private static final Map<UserClientDto, NioSocketChannel>  CHANNELS = new ConcurrentHashMap<>();


    public static void put (Integer appId, String userid, Integer clientType, String imei,  NioSocketChannel nioSocketChannel) {

        UserClientDto userClientDto = new UserClientDto();
        userClientDto.setClientType(clientType);
        userClientDto.setImei(imei);
        userClientDto.setAppId(appId);
        userClientDto.setUserid(userid);

        CHANNELS.put(userClientDto,nioSocketChannel);
    }


    public static NioSocketChannel get(Integer appId,String userId,
                                       Integer clientType,String imei){
        UserClientDto dto = new UserClientDto();
        dto.setImei(imei);
        dto.setAppId(appId);
        dto.setClientType(clientType);
        dto.setUserid(userId);
        return CHANNELS.get(dto);
    }



}
