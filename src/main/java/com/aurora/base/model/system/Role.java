package com.aurora.base.model.system;

import com.aurora.base.model.PageModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 *
 * 角色实体类
 * @author PHQ
 * @create 2020-05-01 16:09
 **/
@Data
@TableName("SYS_ROLE")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper=false)
@Builder
@AllArgsConstructor //全参构造函数
@NoArgsConstructor  //无参构造函数
public class Role extends PageModel {

    @TableId(value = "ID", type = IdType.AUTO)
    private int id;

    @TableField("name")
    private String name;

    @TableField("seq")
    private int seq;

    @TableField("description")
    private String description;

    @TableField("status")
    private int status;

    @TableField(exist = false)
    private List<Resource> rosourceList;


}
