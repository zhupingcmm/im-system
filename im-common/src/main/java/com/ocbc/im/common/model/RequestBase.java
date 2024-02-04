package com.ocbc.im.common.model;

import lombok.Data;

/**
 * @description:
 * @author: lld
 * @version: 1.0
 */
@Data
public class RequestBase {
    private String appId;

    private String operater;

    private Integer clientType;

    private String imei;
}
