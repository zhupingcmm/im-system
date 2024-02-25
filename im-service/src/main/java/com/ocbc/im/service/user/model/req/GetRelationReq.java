package com.ocbc.im.service.user.model.req;

import com.ocbc.im.common.model.RequestBase;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GetRelationReq extends RequestBase {
    @NotBlank(message = "from 用户id不能为空")
    private String fromId;

    @NotBlank(message = "to 用户id不能为空")
    private String toId;
}
