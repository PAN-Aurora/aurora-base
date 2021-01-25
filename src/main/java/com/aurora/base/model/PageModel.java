package com.aurora.base.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页model
 * @author PHQ
 * @create 2020-05-01 11:09
 **/
@Setter
public class PageModel implements Serializable {
    @TableField(exist = false)
    @JSONField(serialize = false)
    @Getter
    private int current = 1;  //当前页

    @TableField(exist = false)
    @JSONField(serialize = false)
    private int start = 0;  //起始位置

    @Getter
    @TableField(exist = false)
    @JSONField(serialize = false)
    private int limit = 10; //每页大小

    @Getter
    @TableField(exist = false)
    @JSONField(serialize = false)
    private int[] ids; //ids集合

    public int getStart() {
        start = (this.current-1) * this.limit;
        return start;
    }
}
