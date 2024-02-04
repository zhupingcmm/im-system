package com.ocbc.im.service.user.controller;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.user.model.req.GetUserInfoReq;
import com.ocbc.im.service.user.model.req.GetUserInfoResp;
import com.ocbc.im.service.user.service.ImUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user/data")
@AllArgsConstructor
public class ImUserDataController {

    private final ImUserService imUserService;

    @PostMapping("/getUserInfo/{appid}")
    public ResponseVO<GetUserInfoResp> getUserInfo(@RequestBody GetUserInfoReq req, @PathVariable(value = "appid") String appId){//@Validated
        req.setAppId(appId);
        return imUserService.getUserInfo(req);
    }
}
