package com.ocbc.im.service.friendship.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.common.enums.ApproverFriendRequestStatusEnum;
import com.ocbc.im.common.enums.FriendShipErrorCode;
import com.ocbc.im.service.friendship.dao.ImFriendShipRequestEntity;
import com.ocbc.im.service.friendship.dao.mapper.ImFriendShipRequestMapper;
import com.ocbc.im.service.friendship.model.req.ApproveFriendRequestReq;
import com.ocbc.im.service.friendship.model.req.FriendDto;
import com.ocbc.im.service.friendship.model.req.ReadFriendShipRequestReq;
import com.ocbc.im.service.friendship.service.ImFriendService;
import com.ocbc.im.service.friendship.service.ImFriendShipRequestService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class ImFriendShipRequestServiceImpl implements ImFriendShipRequestService {

    private final ImFriendShipRequestMapper imFriendShipRequestMapper;

    private final ImFriendService imFriendService;
    @Override
    public ResponseVO addFriendshipRequest(String fromId, FriendDto dto, String appId) {
        QueryWrapper<ImFriendShipRequestEntity> query = new QueryWrapper<>();
        query.eq("app_id", appId);
        query.eq("from_id", fromId);
        query.eq("to_id", dto.getToId());

        ImFriendShipRequestEntity result = imFriendShipRequestMapper.selectOne(query);
        if (ObjectUtils.isEmpty(result)) {
            result = new ImFriendShipRequestEntity();
            result.setAddSource(dto.getAddSource());
            result.setAddWording(dto.getAddWording());
//            result.setSequence(seq);
            result.setAppId(appId);
            result.setFromId(fromId);
            result.setToId(dto.getToId());
            result.setReadStatus(0);
            result.setApproveStatus(0);
            result.setRemark(dto.getRemark());
            result.setCreateTime(LocalDateTime.now());
            imFriendShipRequestMapper.insert(result);
        } else {
            //修改记录内容 和更新时间
            if(StringUtils.isNotBlank(dto.getAddSource())){
                result.setAddSource(dto.getAddSource());
            }
            if(StringUtils.isNotBlank(dto.getRemark())){
                result.setRemark(dto.getRemark());
            }
            if(StringUtils.isNotBlank(dto.getAddWording())){
                result.setAddWording(dto.getAddWording());
            }
//            result.setSequence(seq);
            result.setApproveStatus(0);
            result.setReadStatus(0);

            imFriendShipRequestMapper.updateById(result);

        }


        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO approveFriendRequest(ApproveFriendRequestReq req) {
        ImFriendShipRequestEntity result = imFriendShipRequestMapper.selectById(req.getId());
        if (ObjectUtils.isEmpty(result)) return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_REQUEST_IS_NOT_EXIST);

        if (ObjectUtils.isEmpty(result.getToId())) return ResponseVO.errorResponse(FriendShipErrorCode.NOT_APPROVER_OTHER_MAN_REQUEST);

        ImFriendShipRequestEntity imFriendShipRequestEntity = new ImFriendShipRequestEntity();
        imFriendShipRequestEntity.setId(req.getId());
        imFriendShipRequestEntity.setApproveStatus(req.getStatus());
        imFriendShipRequestEntity.setLastUpdatedTime(LocalDateTime.now());

        imFriendShipRequestMapper.updateById(imFriendShipRequestEntity);

        if (ApproverFriendRequestStatusEnum.AGREE.getCode() == req.getStatus()){

            FriendDto friendDto = new FriendDto();
            friendDto.setAddSource(result.getAddSource());
            friendDto.setRemark(result.getRemark());
            friendDto.setAddWording(result.getAddWording());
            friendDto.setToId(result.getToId());
            imFriendService.doAddFriend(req, result.getFromId(), friendDto, req.getAppId());
        }

        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO readFriendShipRequestReq(ReadFriendShipRequestReq req) {
        QueryWrapper<ImFriendShipRequestEntity> query = new QueryWrapper<>();
        query.eq("app_id", req.getAppId());
        query.eq("to_id", req.getFromId());

        ImFriendShipRequestEntity update = new ImFriendShipRequestEntity();
        // 1 表示已读
        update.setReadStatus(1);
        imFriendShipRequestMapper.update(update, query);

        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO getFriendRequest(String fromId, String appId) {
        QueryWrapper<ImFriendShipRequestEntity> query = new QueryWrapper<>();
        query.eq("app_id", appId);
        query.eq("to_id", fromId);

        List<ImFriendShipRequestEntity> result = imFriendShipRequestMapper.selectList(query);


        return ResponseVO.successResponse(result);
    }
}
