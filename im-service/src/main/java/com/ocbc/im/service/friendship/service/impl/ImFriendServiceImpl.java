package com.ocbc.im.service.friendship.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.common.enums.AllowFriendTypeEnum;
import com.ocbc.im.common.enums.CheckFriendShipTypeEnum;
import com.ocbc.im.common.enums.FriendShipErrorCode;
import com.ocbc.im.common.enums.FriendShipStatusEnum;
import com.ocbc.im.common.model.RequestBase;
import com.ocbc.im.service.friendship.dao.ImFriendShipEntity;
import com.ocbc.im.service.friendship.dao.mapper.ImFriendShipMapper;
import com.ocbc.im.service.friendship.model.req.*;
import com.ocbc.im.service.friendship.model.res.ImportFriendShipResp;
import com.ocbc.im.service.friendship.service.ImFriendService;
import com.ocbc.im.service.user.dao.ImUserDataEntity;
import com.ocbc.im.service.user.model.req.CheckFriendShipReq;
import com.ocbc.im.service.user.model.req.GetAllFriendShipReq;
import com.ocbc.im.service.user.model.req.GetRelationReq;
import com.ocbc.im.service.user.model.resp.CheckFriendShipResp;
import com.ocbc.im.service.user.service.ImUserService;
import lombok.AllArgsConstructor;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ImFriendServiceImpl implements ImFriendService {

    private final ImFriendShipMapper imFriendShipMapper;

    private final ImUserService imUserService;


    @Override
    public ResponseVO importFriendShip(ImportFriendShipReq req) {

        if (req.getFriendItems().size() > 100) {
            return ResponseVO.errorResponse(FriendShipErrorCode.IMPORT_SIZE_BEYOND);
        }

        ImportFriendShipResp importFriendShipResp =  new ImportFriendShipResp();
        List<String> successIds = new ArrayList<>();
        List<String> errorIds = new ArrayList<>();

        for (ImportFriendShipReq.ImportFriendDto friendItem : req.getFriendItems()) {
            ImFriendShipEntity entity = new ImFriendShipEntity();

            BeanUtil.copyProperties(friendItem, entity);
            entity.setAppId(req.getAppId());
            entity.setFromId(req.getFromId());

            try {
                int result = imFriendShipMapper.insert(entity);

                if (result == 1) {
                    successIds.add(friendItem.getToId());
                } else {
                    errorIds.add(friendItem.getToId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                errorIds.add(friendItem.getToId());
            }
        }


        importFriendShipResp.setErrorId(errorIds);
        importFriendShipResp.setSuccessId(successIds);

        return ResponseVO.successResponse(importFriendShipResp);
    }

    @Override
    public ResponseVO addFriend(AddFriendReq req) {
        ResponseVO<ImUserDataEntity> fromInfo = imUserService.getSingleUserInfo(req.getFromId(), req.getAppId());
        if (!fromInfo.isOk()) {
            return fromInfo;
        }

        ResponseVO<ImUserDataEntity> toInfo = imUserService.getSingleUserInfo(req.getToItem().getToId(), req.getAppId());
        if (!toInfo.isOk()) {
            return fromInfo;
        }

        ImUserDataEntity data = toInfo.getData();

        if (data.getFriendAllowType() != null && data.getFriendAllowType() == AllowFriendTypeEnum.NOT_NEED.getCode()) {
            // todo
            this.doAddFriend(req, req.getFromId(), req.getToItem(), req.getAppId());
        } else {
            QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq("app_id", req.getAppId());
            queryWrapper.eq("from_id", req.getFromId());
            queryWrapper.eq("to_id", req.getToItem().getToId());

            ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(queryWrapper);

            if (ObjectUtils.isEmpty(fromItem) || fromItem.getStatus() != FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode()){

//                imFriendShipMapper.insert()

            } else {

                return ResponseVO.errorResponse(FriendShipErrorCode.TO_IS_YOUR_FRIEND);
            }

        }




        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO updateFriend(UpdateFriendReq req) {

        ResponseVO<ImUserDataEntity> fromInfo = imUserService.getSingleUserInfo(req.getFromId(), req.getAppId());
        if (!fromInfo.isOk()) return fromInfo;

        ResponseVO<ImUserDataEntity> toInfo = imUserService.getSingleUserInfo(req.getToItem().getToId(), req.getAppId());
        if (!toInfo.isOk()) return toInfo;

        return this.doUpdate(req.getFromId(), req.getToItem(), req.getAppId());
    }

    @Override
    public ResponseVO deleteFriend(DeleteFriendReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId());
        queryWrapper.eq("from_id", req.getFromId());
        queryWrapper.eq("to_id", req.getToId());

        ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(queryWrapper);

        if (ObjectUtils.isEmpty(fromItem)) {
            return ResponseVO.errorResponse(FriendShipErrorCode.TO_IS_NOT_YOUR_FRIEND);
        } else {
            if (fromItem.getStatus() != null && fromItem.getStatus() == FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode()) {

                ImFriendShipEntity update = new ImFriendShipEntity();
                update.setStatus(FriendShipStatusEnum.FRIEND_STATUS_DELETE.getCode());
                imFriendShipMapper.update(update,queryWrapper);
            } else {
                return ResponseVO.errorResponse(FriendShipErrorCode.FRIEND_IS_DELETED);
            }

        }

        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO deleteAllFriend(DeleteFriendReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId());
        queryWrapper.eq("from_id", req.getFromId());
        queryWrapper.eq("status", FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());

        ImFriendShipEntity update = new ImFriendShipEntity();
        update.setStatus(FriendShipStatusEnum.FRIEND_STATUS_DELETE.getCode());

        imFriendShipMapper.update(update, queryWrapper);

        return ResponseVO.successResponse();
    }


    public ResponseVO doUpdate(String fromId, FriendDto dto,String appId){

        UpdateWrapper<ImFriendShipEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(ImFriendShipEntity::getAddSource,dto.getAddSource())
                .set(ImFriendShipEntity::getExtra,dto.getExtra())
                .set(ImFriendShipEntity::getFriendSequence,1L) //hard code
                .set(ImFriendShipEntity::getRemark,dto.getRemark())
                .eq(ImFriendShipEntity::getAppId,appId)
                .eq(ImFriendShipEntity::getToId,dto.getToId())
                .eq(ImFriendShipEntity::getFromId,fromId);


        int update = imFriendShipMapper.update(null, updateWrapper);

        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO doAddFriend(RequestBase requestBase, String fromId, FriendDto dto, String appId) {

        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId);
        queryWrapper.eq("from_id", fromId);
        queryWrapper.eq("to_id", dto.getToId());

        ImFriendShipEntity fromItem = imFriendShipMapper.selectOne(queryWrapper);
        // 如果不存在， 则执行 添加逻辑
        if (ObjectUtils.isEmpty(fromItem)) {

            fromItem = new ImFriendShipEntity();

            fromItem.setAppId(appId);
            fromItem.setFromId(fromId);

            BeanUtil.copyProperties(dto, fromItem);

            fromItem.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());

            fromItem.setCreateTime(LocalDateTime.now());
            fromItem.setLastUpdatedTime(LocalDateTime.now());
            imFriendShipMapper.insert(fromItem);
        } else {
            // 如果存在，则判断状态， 如果已经添加，则提示已经添加， 如果未添加，则修改状态

            ImFriendShipEntity updateEntity = new ImFriendShipEntity();

            if (StringUtils.isNotBlank(dto.getAddSource())) {
                updateEntity.setAddSource(dto.getAddSource());
            }

            if (StringUtils.isNotBlank(dto.getRemark())) {
                updateEntity.setRemark(dto.getRemark());
            }

            if (StringUtils.isNotBlank(dto.getExtra())) {
                updateEntity.setExtra(dto.getExtra());
            }

            updateEntity.setStatus(FriendShipStatusEnum.FRIEND_STATUS_NORMAL.getCode());

            imFriendShipMapper.update(updateEntity, queryWrapper);
        }
        return ResponseVO.successResponse();
    }

    @Override
    public ResponseVO getAllFriendShip(GetAllFriendShipReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId());
        queryWrapper.eq("from_id", req.getFromId());

        return ResponseVO.successResponse(imFriendShipMapper.selectList(queryWrapper));
    }

    @Override
    public ResponseVO getRelation(GetRelationReq req) {
        QueryWrapper<ImFriendShipEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId());
        queryWrapper.eq("from_id", req.getFromId());
        queryWrapper.eq("to_id", req.getToId());
        List<ImFriendShipEntity> result = imFriendShipMapper.selectList(queryWrapper);

        if (ObjectUtils.isEmpty(result)) {
            return ResponseVO.errorResponse(FriendShipErrorCode.REPEATSHIP_IS_NOT_EXIST);
        }

        return ResponseVO.successResponse(result);
    }

    @Override
    public ResponseVO checkFriendShip(CheckFriendShipReq req) {

        Map<String, Integer> result = req.getToIds()
                .stream()
                .collect(Collectors.toMap(Function.identity(), s -> 0));

        List<CheckFriendShipResp> reqs;

        if (req.getCheckType() == CheckFriendShipTypeEnum.SINGLE.getType()) {
            reqs = imFriendShipMapper.checkFriendShip(req);
        } else {

            reqs = imFriendShipMapper.checkFriendShipBoth(req);
        }

        Map<String, Integer> resultFromDB = reqs.stream()
                .collect(Collectors.toMap(CheckFriendShipResp::getToId,
                        CheckFriendShipResp::getStatus));

        for (String toId : result.keySet()) {
            if (!resultFromDB.containsKey(toId)) {
                CheckFriendShipResp checkFriendShipResp = new CheckFriendShipResp();
                checkFriendShipResp.setFromId(req.getFromId());
                checkFriendShipResp.setToId(toId);
                checkFriendShipResp.setStatus(result.get(toId));
                reqs.add(checkFriendShipResp);
            }
        }

        return ResponseVO.successResponse(reqs);
    }


}
