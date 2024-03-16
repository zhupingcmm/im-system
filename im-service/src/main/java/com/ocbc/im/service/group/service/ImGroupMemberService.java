package com.ocbc.im.service.group.service;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.group.model.req.GroupMemberDto;
import com.ocbc.im.service.group.model.req.RemoveGroupMemberReq;

import java.util.List;

public interface ImGroupMemberService {

    ResponseVO addMember(String groupId, String appId, GroupMemberDto groupMemberDto);


    ResponseVO removeMember(RemoveGroupMemberReq req);


    ResponseVO<List<GroupMemberDto>> getGroupMember(String groupId, String appId);
}
