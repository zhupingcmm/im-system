package com.ocbc.im.tcp.utils;

import com.alibaba.fastjson.JSONObject;
import com.ocbc.im.common.constant.Constants;
import com.ocbc.im.common.enums.ImConnectStatusEnum;
import com.ocbc.im.common.model.UserClientDto;
import com.ocbc.im.common.model.UserSession;
import com.ocbc.im.tcp.redis.RedisManager;
import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;

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


    public static void remove (Integer appid, String userid, Integer clientType, String imei) {
        UserClientDto userClientDto = new UserClientDto();
        userClientDto.setAppId(appid);
        userClientDto.setUserid(userid);
        userClientDto.setClientType(clientType);
        userClientDto.setImei(imei);
        CHANNELS.remove(userClientDto);
    }

    public static void removeSession(NioSocketChannel channel) {
        String userid = (String) channel.attr(AttributeKey.valueOf(Constants.UserId)).get();
        Integer appid = (Integer) channel.attr(AttributeKey.valueOf(Constants.AppId)).get();
        Integer clientType = (Integer) channel.attr(AttributeKey.valueOf(Constants.ClientType)).get();
        String imei = (String) channel.attr(AttributeKey.valueOf(Constants.Imei)).get();
        // remove from memory
        SessionSocketHolder.remove(appid, userid, clientType, imei);

        // remove from redis
        RedissonClient redissonClient = RedisManager.getRedissonClient();
        RMap<Object, Object> map = redissonClient.getMap(appid + Constants.RedisConstants.UserSessionConstants + userid);
        map.remove(clientType + ":" + imei);

        // todo send message to kafka

        channel.close();
    }

    public static void offlineSession(NioSocketChannel channel) {
        String userid = (String) channel.attr(AttributeKey.valueOf(Constants.UserId)).get();
        Integer appid = (Integer) channel.attr(AttributeKey.valueOf(Constants.AppId)).get();
        Integer clientType = (Integer) channel.attr(AttributeKey.valueOf(Constants.ClientType)).get();
        String imei = (String) channel.attr(AttributeKey.valueOf(Constants.Imei)).get();
        // remove from memory
        SessionSocketHolder.remove(appid, userid, clientType, imei);

        // update from redis
        RedissonClient redissonClient = RedisManager.getRedissonClient();
        RMap<Object, Object> map = redissonClient.getMap(appid + Constants.RedisConstants.UserSessionConstants + userid);
        String sessionStr = (String) map.get(clientType + ":" + imei);

        if (StringUtils.isNotBlank(sessionStr)) {
            UserSession userSession = JSONObject.parseObject(sessionStr, UserSession.class);
            userSession.setConnectState(ImConnectStatusEnum.OFFLINE_STATUS.getCode());
            map.put(clientType + ":" + imei, JSONObject.toJSONString(userSession));
        }

        // todo send message to kafka

        channel.close();
    }







}
