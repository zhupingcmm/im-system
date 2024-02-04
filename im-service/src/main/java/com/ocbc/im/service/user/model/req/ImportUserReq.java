package com.ocbc.im.service.user.model.req;

import com.ocbc.im.common.model.RequestBase;
import com.ocbc.im.service.user.dao.ImUserDataEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class ImportUserReq extends RequestBase {

    private List<ImUserDataDTO> userData;
}
