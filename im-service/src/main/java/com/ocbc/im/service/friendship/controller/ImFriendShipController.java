package com.ocbc.im.service.friendship.controller;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.friendship.model.req.AddFriendReq;
import com.ocbc.im.service.friendship.model.req.DeleteFriendReq;
import com.ocbc.im.service.friendship.model.req.ImportFriendShipReq;
import com.ocbc.im.service.friendship.model.req.UpdateFriendReq;
import com.ocbc.im.service.friendship.service.ImFriendService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/friendship")
@AllArgsConstructor
public class ImFriendShipController {

    private final ImFriendService imFriendService;

    @PostMapping("/add/{appid}")
    public ResponseVO addFriendShip(@RequestBody ImportFriendShipReq req, @PathVariable(value = "appid") String appId) {
        req.setAppId(appId);
        return imFriendService.importFriendShip(req);
    }


    @PostMapping("/addfriend/{appid}")
    public ResponseVO addFriend(@RequestBody AddFriendReq req,  @PathVariable(value = "appid")String appId) {
        req.setAppId(appId);
        return imFriendService.addFriend(req);
    }

    @PutMapping("/updatefriend/{appid}")
    public ResponseVO updateFriend(@RequestBody @Validated UpdateFriendReq req, @PathVariable(value = "appid")String appId){
        req.setAppId(appId);
        return imFriendService.updateFriend(req);
    }

    @DeleteMapping("/delete/{appid}")
    public ResponseVO deleteFriend(@RequestBody @Validated DeleteFriendReq req, @PathVariable(value = "appid")String appId){
        req.setAppId(appId);
        return imFriendService.deleteFriend(req);
    }

    @DeleteMapping("/deleteallfriend/{appid}")
    public ResponseVO deleteAllFriend(@RequestBody @Validated DeleteFriendReq req,  @PathVariable(value = "appid")String appId){
        req.setAppId(appId);
        return imFriendService.deleteAllFriend(req);
    }




}
