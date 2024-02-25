package com.ocbc.im.service.friendship.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocbc.im.service.friendship.dao.ImFriendShipEntity;
import com.ocbc.im.service.user.model.req.CheckFriendShipReq;
import com.ocbc.im.service.user.model.resp.CheckFriendShipResp;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ImFriendShipMapper extends BaseMapper<ImFriendShipEntity> {


    @Select("<script> select from_id as fromId, to_id as toId, (case when status = 1 then 1 when status !=1 then 0 end) as status from im_friendship where from_id = #{fromId} and to_id in" +
            "<foreach collection = 'toIds' index='index' item='id' separator=',' close = ')' open='('>" +
            "#{id}" +
            " </foreach>" +
            " and app_id = #{appId}" +
            " </script>")
    List<CheckFriendShipResp> checkFriendShip(CheckFriendShipReq req);


    @Select("<script>" +
            " select a.fromId,a.toId , ( \n" +
            " case \n" +
            " when a.status = 1 and b.status = 1 then 1 \n" +
            " when a.status = 1 and b.status != 1 then 2 \n" +
            " when a.status != 1 and b.status = 1 then 3 \n" +
            " when a.status != 1 and b.status != 1 then 4 \n" +
            " end \n" +
            " ) \n " +
            " as status from "+
            " (select from_id AS fromId , to_id AS toId , (case when status = 1 then 1 when status !=1 then 0 end) as status from im_friendship where app_id = #{appId} and from_id = #{fromId} AND to_id in " +
            "<foreach collection='toIds' index='index' item='id' separator=',' close=')' open='('>" +
            " #{id} " +
            "</foreach>" +
            " ) as a INNER join" +
            " (select from_id AS fromId, to_id AS toId , (case when status = 1 then 1 when status !=1 then 0 end) as status from im_friendship where app_id = #{appId} and to_id = #{fromId} AND from_id in " +
            "<foreach collection='toIds' index='index' item='id' separator=',' close=')' open='('>" +
            " #{id} " +
            "</foreach>" +
            " ) as b " +
            " on a.fromId = b.toId AND b.fromId = a.toId "+
            "</script>"
    )
    List<CheckFriendShipResp> checkFriendShipBoth(CheckFriendShipReq req);
}
