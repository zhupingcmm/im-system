package com.ocbc.im.common.model;

import lombok.Data;

@Data
public class UserSession {

    private String userid;

    private Integer appId;

    private Integer clientType;

    private Integer version;

    private Integer connectState;

    private String imei;
}
