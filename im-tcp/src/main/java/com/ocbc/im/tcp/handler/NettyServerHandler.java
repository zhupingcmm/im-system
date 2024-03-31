package com.ocbc.im.tcp.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.ocbc.im.cdec.pack.LoginPack;
import com.ocbc.im.cdec.pack.user.LoginAckPack;
import com.ocbc.im.cdec.proto.Message;
import com.ocbc.im.cdec.proto.MessagePack;
import com.ocbc.im.common.constant.Constants;
import com.ocbc.im.common.enums.ImConnectStatusEnum;
import com.ocbc.im.common.enums.command.SystemCommand;
import com.ocbc.im.common.model.UserSession;
import com.ocbc.im.tcp.utils.SessionSocketHolder;
import com.ocbc.im.tcp.redis.RedisManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServerHandler extends SimpleChannelInboundHandler<Message> {

    private final static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message message) throws Exception {
        logger.info("message: {}", message);

        Integer command = message.getMessageHeader().getCommand();

        if  (command ==  SystemCommand.LOGIN.getCommand()) {
            LoginPack loginPack = JSON.parseObject(JSONObject.toJSONString(message.getMessagePack()), new TypeReference<LoginPack>() {}.getType());
            /** 登陸事件 **/
            String userId = loginPack.getUserid();
            /** 为channel设置用户id **/
            ctx.channel().attr(AttributeKey.valueOf(Constants.UserId)).set(userId);
            String clientImei = message.getMessageHeader().getClientType() + ":" + message.getMessageHeader().getImei();
            /** 为channel设置client和imel **/
            ctx.channel().attr(AttributeKey.valueOf(Constants.ClientImei)).set(clientImei);
            /** 为channel设置appId **/
            ctx.channel().attr(AttributeKey.valueOf(Constants.AppId)).set(message.getMessageHeader().getAppId());
            /** 为channel设置ClientType **/
            ctx.channel().attr(AttributeKey.valueOf(Constants.ClientType))
                    .set(message.getMessageHeader().getClientType());
            /** 为channel设置Imei **/
            ctx.channel().attr(AttributeKey.valueOf(Constants.Imei))
                    .set(message.getMessageHeader().getImei());


            UserSession userSession = new UserSession();
            userSession.setAppId(message.getMessageHeader().getAppId());
            userSession.setClientType(message.getMessageHeader().getClientType());
            userSession.setUserid(loginPack.getUserid());
            userSession.setConnectState(ImConnectStatusEnum.ONLINE_STATUS.getCode());
//            userSession.setBrokerId(brokerId);
            userSession.setImei(message.getMessageHeader().getImei());

//            try {
//                InetAddress localHost = InetAddress.getLocalHost();
//                userSession.setBrokerHost(localHost.getHostAddress());
//            }catch (Exception e){
//                e.printStackTrace();
//            }

            // store in redis
            RedissonClient redissonClient = RedisManager.getRedissonClient();
            RMap<Object, Object> map = redissonClient.getMap(message.getMessageHeader().getAppId() + Constants.RedisConstants.UserSessionConstants + loginPack.getUserid());
            map.put(message.getMessageHeader().getClientType()+":" + message.getMessageHeader().getImei()
                    ,JSONObject.toJSONString(userSession));

            // store in channel
            SessionSocketHolder
                    .put(message.getMessageHeader().getAppId()
                            ,loginPack.getUserid(),
                            message.getMessageHeader().getClientType(),message.getMessageHeader().getImei(),(NioSocketChannel) ctx.channel());

            // return to client
            MessagePack<LoginAckPack> loginSuccess = new MessagePack<>();

            LoginAckPack loginAckPack = new LoginAckPack();
            loginAckPack.setUserid(loginPack.getUserid());

            loginSuccess.setCommand(SystemCommand.LOGIN.getCommand());
            loginSuccess.setData(loginAckPack);
            loginSuccess.setImei(message.getMessageHeader().getImei());
            loginSuccess.setAppId(message.getMessageHeader().getAppId());
            ctx.channel().writeAndFlush(loginSuccess);

        } else if (command == SystemCommand.LOGOUT.getCommand()) {
            SessionSocketHolder.removeSession((NioSocketChannel) ctx.channel());
        } else if (command == SystemCommand.PING.getCommand()){
            ctx.channel().attr(AttributeKey.valueOf(Constants.ReadTime)).set(System.currentTimeMillis());
        }


    }

}
