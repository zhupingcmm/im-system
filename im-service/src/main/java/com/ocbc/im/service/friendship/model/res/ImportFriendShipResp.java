package com.ocbc.im.service.friendship.model.res;

import lombok.Data;

import java.util.List;

@Data
public class ImportFriendShipResp {
    private List<String> successId;

    private List<String> errorId;
}
