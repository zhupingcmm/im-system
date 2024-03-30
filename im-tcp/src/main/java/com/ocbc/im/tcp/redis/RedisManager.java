package com.ocbc.im.tcp.redis;

import com.ocbc.im.cdec.config.BootstrapConfig;
import org.redisson.api.RedissonClient;

public class RedisManager {

    private static RedissonClient redissonClient;

    private static Integer loginModel;

    public static void init(BootstrapConfig config) {
        loginModel = config.getLim().getLoginModel();
        SingleClientStrategy singleClientStrategy = new SingleClientStrategy();
        redissonClient = singleClientStrategy.getRedissonClient(config.getLim().getRedis());
    }

    public static RedissonClient getRedissonClient(){
        return redissonClient;
    }
}
