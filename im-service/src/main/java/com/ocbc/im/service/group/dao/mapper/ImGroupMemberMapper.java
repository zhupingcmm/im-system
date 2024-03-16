package com.ocbc.im.service.group.dao.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ocbc.im.service.group.dao.ImGroupMemberEntity;
import com.ocbc.im.service.group.model.req.GroupMemberDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImGroupMemberMapper extends BaseMapper<ImGroupMemberEntity> {


    @Select("select id as memberId, speak_date as speakDate ,role as role, alias as alias, join_time as joinTime, join_type as joinType from im_group_member where app_id = #{appId} and group_id = #{groupId}")
    List<GroupMemberDto> getGroupMember(String appid, String groupId);

}
