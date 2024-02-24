package com.ocbc.im.service.friendship.dao;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("im_friendship")
public class ImFriendShipEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "app_id")
    private String appId;

    @TableField(value = "from_id")
    private String fromId;

    @TableField(value = "to_id")
    private String toId;

    /** 备注*/
    private String remark;
    /** 状态 1正常 0删除*/
    private Integer status;
    /** 状态 1正常 0拉黑*/
    private Integer black;
    //    @TableField(updateStrategy = FieldStrategy.IGNORED)

    /** 好友关系序列号*/
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long friendSequence;

    /** 黑名单关系序列号*/
    private Long blackSequence;
    /** 好友来源*/
//    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String addSource;

    private String extra;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdatedTime;



}
