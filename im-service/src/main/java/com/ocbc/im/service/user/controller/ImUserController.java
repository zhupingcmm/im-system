package com.ocbc.im.service.user.controller;

import com.ocbc.im.common.ResponseVO;
import com.ocbc.im.service.user.model.req.GetUserInfoReq;
import com.ocbc.im.service.user.model.req.GetUserInfoResp;
import com.ocbc.im.service.user.model.req.ImportUserReq;
import com.ocbc.im.service.user.model.resp.ImportUserResp;
import com.ocbc.im.service.user.service.ImUserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
@AllArgsConstructor
public class ImUserController {

    private final ImUserService imUserService;

    @PostMapping("add/{appid}")
    public ResponseVO<ImportUserResp> addUsers(@RequestBody ImportUserReq req, @PathVariable("appid") String appId) {
        req.setAppId(appId);
        return imUserService.importUser(req);
    }


}
