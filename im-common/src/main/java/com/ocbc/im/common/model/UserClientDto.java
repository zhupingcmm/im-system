package com.ocbc.im.common.model;

import lombok.Data;

@Data
public class UserClientDto {
    private Integer appId;
    private Integer clientType;
    private String userid;
    private String imei;

}
