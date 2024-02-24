package com.ocbc.im.service.user.service;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.user.dao.ImUserDataEntity;
import com.ocbc.im.service.user.model.req.*;
import com.ocbc.im.service.user.model.resp.ImportUserResp;

public interface ImUserService {
    ResponseVO<ImportUserResp> importUser(ImportUserReq req);

    ResponseVO<GetUserInfoResp> getUserInfo(GetUserInfoReq req);

    ResponseVO<ImUserDataEntity> getSingleUserInfo(String userId , String appId);

    ResponseVO deleteUser(DeleteUserReq req);

    ResponseVO modifyUserInfo(ModifyUserInfoReq req);

    ResponseVO login(LoginReq req);

    ResponseVO getUserSequence(GetUserSequenceReq req);
}
