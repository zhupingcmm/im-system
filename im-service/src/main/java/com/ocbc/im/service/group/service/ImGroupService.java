package com.ocbc.im.service.group.service;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.group.model.req.DestroyGroupReq;
import com.ocbc.im.service.group.model.req.GetGroupReq;
import com.ocbc.im.service.group.model.req.ImportGroupReq;

public interface ImGroupService {
    ResponseVO importGroup(ImportGroupReq req);

    ResponseVO createGroup(ImportGroupReq req);

    ResponseVO destroyGroup(DestroyGroupReq req);


    ResponseVO getGroup(String groupId, String appId);


    ResponseVO getGroup(GetGroupReq req);
}

