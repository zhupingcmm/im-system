package com.ocbc.im.service.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.common.enums.GroupErrorCode;
import com.ocbc.im.common.enums.GroupMemberRoleEnum;
import com.ocbc.im.common.enums.GroupStatusEnum;
import com.ocbc.im.service.group.dao.ImGroupMemberEntity;
import com.ocbc.im.service.group.dao.mapper.ImGroupMemberMapper;
import com.ocbc.im.service.group.model.req.GroupMemberDto;
import com.ocbc.im.service.group.model.req.RemoveGroupMemberReq;
import com.ocbc.im.service.group.service.ImGroupMemberService;
import com.ocbc.im.service.user.dao.ImUserDataEntity;
import com.ocbc.im.service.user.service.ImUserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ImGroupMemberServiceImpl implements ImGroupMemberService {

    private final ImUserService imUserService;

    private final ImGroupMemberMapper imGroupMemberMapper;

    @Override
    public ResponseVO addMember(String groupId, String appId, GroupMemberDto groupMemberDto) {
        ResponseVO<ImUserDataEntity> singleUserInfo = imUserService.getSingleUserInfo(groupMemberDto.getMemberId(), appId);
        // 如果 user 不存在，直接返回
        if (!singleUserInfo.isOk()) {
            return singleUserInfo;
        }

        // 判断群是否有 owner
        if (groupMemberDto.getRole() != null && GroupMemberRoleEnum.OWNER.getCode() == groupMemberDto.getRole()) {
            QueryWrapper<ImGroupMemberEntity> query = new QueryWrapper<>();
            query.eq("group_id", groupId);
            query.eq("app_id", appId);
            query.eq("role", GroupMemberRoleEnum.OWNER.getCode());
            Long count = imGroupMemberMapper.selectCount(query);

            if (count > 0 ) return ResponseVO.errorResponse(GroupErrorCode.GROUP_IS_HAVE_OWNER);
        }


        QueryWrapper<ImGroupMemberEntity> query = new QueryWrapper<>();
        query.eq("group_id", groupId);
        query.eq("app_id", appId);
        query.eq("role", GroupMemberRoleEnum.OWNER.getCode());

        ImGroupMemberEntity imGroupMemberEntity = imGroupMemberMapper.selectOne(query);


        if (ObjectUtils.isEmpty(imGroupMemberEntity)) {
            // 初次加群
            imGroupMemberEntity = new ImGroupMemberEntity();
            BeanUtil.copyProperties(groupMemberDto, imGroupMemberEntity);
            imGroupMemberEntity.setGroupId(groupId);
            imGroupMemberEntity.setAppId(appId);
            imGroupMemberEntity.setJoinTime(LocalDateTime.now());
            int insert = imGroupMemberMapper.insert(imGroupMemberEntity);
            if (insert == 1) {
                return ResponseVO.successResponse();
            }

        } else {
            // 重新进入

            BeanUtil.copyProperties(groupMemberDto, imGroupMemberEntity);
            imGroupMemberEntity.setJoinTime(LocalDateTime.now());
            int update = imGroupMemberMapper.update(imGroupMemberEntity, query);
            if (update == 1) {
                return ResponseVO.successResponse();
            }
        }
        return ResponseVO.errorResponse(GroupErrorCode.USER_IS_JOINED_GROUP);

    }

    @Override
    public ResponseVO removeMember(RemoveGroupMemberReq req) {
        boolean isAdmin = false;
        return null;
    }

    @Override
    public ResponseVO<List<GroupMemberDto>> getGroupMember(String groupId, String appId) {
        return ResponseVO.successResponse(imGroupMemberMapper.getGroupMember(appId, groupId));
    }
}
