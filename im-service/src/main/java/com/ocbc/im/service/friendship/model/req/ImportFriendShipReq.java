package com.ocbc.im.service.friendship.model.req;

import com.ocbc.im.common.enums.FriendShipStatusEnum;
import com.ocbc.im.common.model.RequestBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.util.List;


@Data
@EqualsAndHashCode(callSuper = true)
public class ImportFriendShipReq extends RequestBase {

    @NotBlank(message = "fromId can not blank")
    private String fromId;

    private List<ImportFriendDto> friendItems;





    @Data
    public static class ImportFriendDto{

        private String toId;

        private String remark;

        private String addSource;

        private Integer status = FriendShipStatusEnum.FRIEND_STATUS_NO_FRIEND.getCode();

        private Integer black = FriendShipStatusEnum.BLACK_STATUS_NORMAL.getCode();

    }
}
