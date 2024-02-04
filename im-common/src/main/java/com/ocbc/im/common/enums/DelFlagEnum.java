package com.ocbc.im.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DelFlagEnum {

    /**
     * 0 正常； 1 删除
     */

    NORMAL(1),
    DELETE(0);

    private final int code;
}
