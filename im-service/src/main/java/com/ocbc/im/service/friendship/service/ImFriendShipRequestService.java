package com.ocbc.im.service.friendship.service;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.friendship.model.req.ApproveFriendRequestReq;
import com.ocbc.im.service.friendship.model.req.FriendDto;
import com.ocbc.im.service.friendship.model.req.ReadFriendShipRequestReq;

public interface ImFriendShipRequestService {
    ResponseVO addFriendshipRequest(String fromId, FriendDto dto, String appId);

     ResponseVO approveFriendRequest(ApproveFriendRequestReq req);

     ResponseVO readFriendShipRequestReq(ReadFriendShipRequestReq req);

     ResponseVO getFriendRequest(String fromId, String appId);
}
