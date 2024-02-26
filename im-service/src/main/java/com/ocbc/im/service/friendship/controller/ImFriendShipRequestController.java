package com.ocbc.im.service.friendship.controller;


import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.friendship.model.req.ApproveFriendRequestReq;
import com.ocbc.im.service.friendship.model.req.FriendDto;
import com.ocbc.im.service.friendship.model.req.GetFriendShipRequestReq;
import com.ocbc.im.service.friendship.model.req.ReadFriendShipRequestReq;
import com.ocbc.im.service.friendship.service.ImFriendShipRequestService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("v1/friendshipRequest")
@AllArgsConstructor
public class ImFriendShipRequestController {


    private final ImFriendShipRequestService imFriendShipRequestService;

    @PostMapping("/addFriendshipRequest/{fromId}/{appId}")
    public ResponseVO addFriendshipRequest(@PathVariable String fromId, @PathVariable String appId, @RequestBody FriendDto dto){
        return imFriendShipRequestService.addFriendshipRequest(fromId, dto, appId);
    }

    @PutMapping("/approveFriendRequest/{appId}")
    public ResponseVO approveFriendRequest(@RequestBody @Validated
                                               ApproveFriendRequestReq req, @PathVariable String appId, String identifier){
        req.setAppId(appId);
        req.setOperator(identifier);
        return imFriendShipRequestService.approveFriendRequest(req);
    }
    @GetMapping("/getFriendRequest/{appId}")
    public ResponseVO getFriendRequest(@RequestBody @Validated GetFriendShipRequestReq req, @PathVariable String appId){
        req.setAppId(appId);
        return imFriendShipRequestService.getFriendRequest(req.getFromId(),req.getAppId());
    }

    @PutMapping("/readFriendShipRequestReq/{appId}")
    public ResponseVO readFriendShipRequestReq(@RequestBody @Validated ReadFriendShipRequestReq req, @PathVariable String appId){
        req.setAppId(appId);
        return imFriendShipRequestService.readFriendShipRequestReq(req);
    }



}
