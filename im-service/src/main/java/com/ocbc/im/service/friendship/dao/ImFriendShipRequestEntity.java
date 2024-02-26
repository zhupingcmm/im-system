package com.ocbc.im.service.friendship.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@Data
@TableName("im_friendship_request")
public class ImFriendShipRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String appId;

    private String fromId;

    private String toId;
    /** 备注*/
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    //是否已读 1已读
    private Integer readStatus;

    /** 好友来源*/

    private String addSource;


    private String addWording;

    //审批状态 1同意 2拒绝
    private Integer approveStatus;

    /** 序列号*/
    private Long sequence;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdatedTime;




}
