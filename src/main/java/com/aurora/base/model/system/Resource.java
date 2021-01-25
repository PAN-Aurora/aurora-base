package com.aurora.base.model.system;

import com.aurora.base.model.PageModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 *  资源实体
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@Data
@TableName(value = "SYS_RESOURCE",autoResultMap = true)
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class Resource extends PageModel {

    @TableId(value = "ID", type = IdType.AUTO)
    private int id;

    @TableField(value = "pid")
    private int pid;

    @TableField( value = "name")
    private String name;

    @TableField( value = "seq")
    private int seq;

    @TableField( value = "module")
    private String module;

    @TableField( value = "url")
    private String url;

    @TableField( value = "status")
    private int status;

    @TableField( value = "createdate")
    private Date createdate;

    @TableField( value = "type")
    private int type;

    @TableField(exist = false)
    private List<Resource> childResourceList;


}
