package com.ocbc.im.service.friendship.service;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.common.model.RequestBase;
import com.ocbc.im.service.friendship.model.req.*;

public interface ImFriendService {

    ResponseVO importFriendShip(ImportFriendShipReq req);

    ResponseVO addFriend(AddFriendReq req);

    ResponseVO updateFriend(UpdateFriendReq req);

    ResponseVO deleteFriend(DeleteFriendReq req);

    ResponseVO deleteAllFriend(DeleteFriendReq req);

    ResponseVO doAddFriend(RequestBase requestBase, String fromId, FriendDto dto, String appId);
}
