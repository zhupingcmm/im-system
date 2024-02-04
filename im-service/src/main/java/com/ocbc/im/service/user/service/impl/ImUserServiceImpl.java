package com.ocbc.im.service.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.common.enums.DelFlagEnum;
import com.ocbc.im.common.enums.UserErrorCode;
import com.ocbc.im.common.utils.DateUtil;
import com.ocbc.im.service.user.dao.ImUserDataEntity;
import com.ocbc.im.service.user.dao.mapper.ImUserDataMapper;
import com.ocbc.im.service.user.model.req.*;
import com.ocbc.im.service.user.model.resp.ImportUserResp;
import com.ocbc.im.service.user.service.ImUserService;
import lombok.AllArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ImUserServiceImpl implements ImUserService {

    private final ImUserDataMapper imUserDataMapper;
    @Override
    public ResponseVO<ImportUserResp> importUser(ImportUserReq req) {
        if (req.getUserData().size() > 100) {
            return ResponseVO.errorResponse(UserErrorCode.IMPORT_SIZE_BEYOND);
        }

        ImportUserResp importUserResp = new ImportUserResp();
        List<String> successIds = new ArrayList<>();
        List<String> errorIds = new ArrayList<>();

        for (ImUserDataDTO userDatum : req.getUserData()) {
            ImUserDataEntity imUserDataEntity = new ImUserDataEntity();
            BeanUtil.copyProperties(userDatum, imUserDataEntity);
            imUserDataEntity.setAppId(req.getAppId());
            imUserDataEntity.setBirthDay(DateUtil.parse(userDatum.getBirthDay()));

            try {

                int inserted = imUserDataMapper.insert(imUserDataEntity);
                if (inserted == 1) {
                    successIds.add(userDatum.getUserId());
                }

            } catch (Exception ex) {
                errorIds.add(userDatum.getUserId());
            }

        }

        importUserResp.setErrorIds(successIds);
        importUserResp.setErrorIds(errorIds);
        return null;
    }

    @Override
    public ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req) {
        QueryWrapper<ImUserDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", req.getAppId());
        queryWrapper.in("user_id", req.getUserIds());
        queryWrapper.eq("is_active", DelFlagEnum.NORMAL.getCode());

        List<ImUserDataEntity> imUserDataEntities = imUserDataMapper.selectList(queryWrapper);

        Map<String, ImUserDataEntity> map = new HashMap<>();

        imUserDataEntities.forEach(imUserDataEntity -> {
           map.put(imUserDataEntity.getUserId(), imUserDataEntity);
        });

        List<String> failUser = new ArrayList<>();

        for (String uid: req.getUserIds()) {
            if (!map.containsKey(uid)) failUser.add(uid);
        }
        GetUserInfoResp getUserInfoResp = new GetUserInfoResp();
        getUserInfoResp.setUserDataItem(imUserDataEntities);
        getUserInfoResp.setFailUser(failUser);

        return ResponseVO.successResponse(getUserInfoResp);
    }

    @Override
    public ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId, Integer appId) {
        QueryWrapper<ImUserDataEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("app_id", appId);
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("is_active", DelFlagEnum.NORMAL.getCode());
        ImUserDataEntity imUserDataEntity = imUserDataMapper.selectOne(queryWrapper);
        if (ObjectUtils.isEmpty(imUserDataEntity)) {
            return ResponseVO.successResponse(UserErrorCode.USER_IS_NOT_EXIST);
        }

        return ResponseVO.successResponse(imUserDataEntity);
    }

    @Override
    public ResponseVO<ImportUserResp> deleteUser(DeleteUserReq req) {

        ImUserDataEntity userDataEntity = new ImUserDataEntity();
        userDataEntity.setIsActive(DelFlagEnum.DELETE.getCode());

        List<String> errorIds = new ArrayList();
        List<String> successIds = new ArrayList();

        for (String userId : req.getUserIds()) {

            QueryWrapper<ImUserDataEntity> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("app_id", req.getAppId());
            queryWrapper.eq("user_id", userId);
            queryWrapper.eq("is_active", DelFlagEnum.NORMAL.getCode());

            try {
                int update = imUserDataMapper.update(userDataEntity, queryWrapper);
                if(update > 0){
                    successIds.add(userId);
                }else{
                    errorIds.add(userId);
                }

            } catch (Exception e) {
                errorIds.add(userId);
            }
        }
        ImportUserResp importUserResp = new ImportUserResp();
        importUserResp.setErrorIds(errorIds);
        importUserResp.setSuccessIds(successIds);

        return ResponseVO.successResponse(importUserResp);
    }

    @Override
    public ResponseVO modifyUserInfo(ModifyUserInfoReq req) {
        return null;
    }

    @Override
    public ResponseVO login(LoginReq req) {
        return null;
    }

    @Override
    public ResponseVO getUserSequence(GetUserSequenceReq req) {
        return null;
    }
}
