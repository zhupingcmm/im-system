package com.ocbc.im.service.friendship.service.impl;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.friendship.model.req.ApproveFriendRequestReq;
import com.ocbc.im.service.friendship.model.req.FriendDto;
import com.ocbc.im.service.friendship.model.req.ReadFriendShipRequestReq;
import com.ocbc.im.service.friendship.service.ImFriendShipRequestService;

public class ImFriendShipRequestServiceImpl implements ImFriendShipRequestService {
    @Override
    public ResponseVO addFriendshipRequest(String fromId, FriendDto dto, Integer appId) {
        return null;
    }

    @Override
    public ResponseVO approveFriendRequest(ApproveFriendRequestReq req) {
        return null;
    }

    @Override
    public ResponseVO readFriendShipRequestReq(ReadFriendShipRequestReq req) {
        return null;
    }

    @Override
    public ResponseVO getFriendRequest(String fromId, Integer appId) {
        return null;
    }
}
