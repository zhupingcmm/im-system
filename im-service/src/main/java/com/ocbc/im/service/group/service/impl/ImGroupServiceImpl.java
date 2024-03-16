package com.ocbc.im.service.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.common.enums.GroupErrorCode;
import com.ocbc.im.common.enums.GroupMemberRoleEnum;
import com.ocbc.im.common.enums.GroupStatusEnum;
import com.ocbc.im.common.enums.GroupTypeEnum;
import com.ocbc.im.common.exception.ApplicationException;
import com.ocbc.im.common.utils.DateUtil;
import com.ocbc.im.service.group.dao.ImGroupEntity;
import com.ocbc.im.service.group.dao.mapper.ImGroupMapper;
import com.ocbc.im.service.group.model.req.DestroyGroupReq;
import com.ocbc.im.service.group.model.req.GetGroupReq;
import com.ocbc.im.service.group.model.req.GroupMemberDto;
import com.ocbc.im.service.group.model.req.ImportGroupReq;
import com.ocbc.im.service.group.model.resp.GetGroupResp;
import com.ocbc.im.service.group.service.ImGroupMemberService;
import com.ocbc.im.service.group.service.ImGroupService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImGroupServiceImpl implements ImGroupService {

    private final ImGroupMapper imGroupMapper;

    private final ImGroupMemberService imGroupMemberService;
    @Override
    public ResponseVO importGroup(ImportGroupReq req) {
        checkGroupId(req);


        ImGroupEntity imGroupEntity = new ImGroupEntity();
        imGroupEntity.setStatus(GroupStatusEnum.NORMAL.getCode());
        BeanUtil.copyProperties(req, imGroupEntity);

        if (StringUtils.isEmpty(req.getCreateTime())) {
            imGroupEntity.setCreateTime(LocalDateTime.now());
        } else {
            imGroupEntity.setCreateTime(DateUtil.parse(req.getCreateTime()));
        }

        int result = imGroupMapper.insert(imGroupEntity);
        if (result != 1) throw new ApplicationException(GroupErrorCode.IMPORT_GROUP_ERROR);

        return ResponseVO.successResponse();
    }

    private void checkGroupId(ImportGroupReq req) {
        QueryWrapper<ImGroupEntity> queryWrapper = new QueryWrapper<>();

        if(StringUtils.isEmpty(req.getId())){
            req.setId(UUID.randomUUID().toString().replace("-",""));
        } else {
            queryWrapper.eq("id", req.getId());

            Long count = imGroupMapper.selectCount(queryWrapper);

            if (count > 0) throw new ApplicationException(GroupErrorCode.GROUP_IS_EXIST);
        }

        if (req.getGroupType() == GroupTypeEnum.PUBLIC.getCode() && StringUtils.isBlank(req.getOwnerId())) {
            throw new ApplicationException(GroupErrorCode.PUBLIC_GROUP_MUST_HAVE_OWNER);
        }
    }

    @Override
    public ResponseVO createGroup(ImportGroupReq req) {
        boolean isAdmin = false;
        if (!isAdmin) {
            req.setOwnerId(req.getOperator());
        }

        checkGroupId(req);
        ImGroupEntity imGroupEntity = new ImGroupEntity();

        BeanUtil.copyProperties(req, imGroupEntity);
        imGroupEntity.setCreateTime(LocalDateTime.now());
        imGroupEntity.setStatus(GroupStatusEnum.NORMAL.getCode());

        int result = imGroupMapper.insert(imGroupEntity);
        if (result != 1) throw new ApplicationException(GroupErrorCode.IMPORT_GROUP_ERROR);

        // 先添加 owner
        GroupMemberDto dto = new GroupMemberDto();
        dto.setMemberId(req.getOwnerId());
        dto.setRole(GroupMemberRoleEnum.OWNER.getCode());
        dto.setJoinTime(LocalDateTime.now());

        imGroupMemberService.addMember(req.getId(), req.getAppId(), dto);

        req.getMember().forEach(m -> imGroupMemberService.addMember(req.getId(), req.getAppId(), m));

        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO destroyGroup(DestroyGroupReq req) {
        boolean isAdmin = false;
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        query.eq("group_id", req.getGroupId());
        query.eq("app_id", req.getAppId());
        ImGroupEntity imGroupEntity = imGroupMapper.selectOne(query);

        if (ObjectUtils.isEmpty(imGroupEntity)) throw new ApplicationException(GroupErrorCode.PRIVATE_GROUP_CAN_NOT_DESTORY);

        if (imGroupEntity.getStatus() == GroupStatusEnum.DESTROY.getCode()) throw new ApplicationException(GroupErrorCode.GROUP_IS_DESTROY);

        if (!isAdmin) {
            if (imGroupEntity.getGroupType() == GroupTypeEnum.PUBLIC.getCode()) {
                throw new ApplicationException(GroupErrorCode.THIS_OPERATE_NEED_OWNER_ROLE);
            }

        }

        ImGroupEntity update = new ImGroupEntity();
        update.setStatus(GroupStatusEnum.DESTROY.getCode());
        int result = imGroupMapper.update(update, query);
        if (result != 1) {
            throw new ApplicationException(GroupErrorCode.UPDATE_GROUP_BASE_INFO_ERROR);
        }


        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO getGroup(String groupId, String appId) {
        QueryWrapper<ImGroupEntity> query = new QueryWrapper<>();
        query.eq("id", groupId);
        query.eq("app_id", appId);

        ImGroupEntity imGroupEntity = imGroupMapper.selectOne(query);

        if (ObjectUtils.isEmpty(imGroupEntity)) return ResponseVO.errorResponse(GroupErrorCode.GROUP_IS_NOT_EXIST);


        return ResponseVO.successResponse(imGroupEntity);
    }

    @Override
    public ResponseVO getGroup(GetGroupReq req) {

        ResponseVO groupResponse = this.getGroup(req.getGroupId(), req.getAppId());

        if (!groupResponse.isOk()) return groupResponse;

        GetGroupResp getGroupResp = new GetGroupResp();



        return null;
    }
}
