package com.aurora.modules.model.status;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
@TableName(value = "TBL_PROJECT",autoResultMap=true)
public class Project {

    @TableId(value = "PROJECT_ID",type = IdType.AUTO)
    private Integer projectId;

    private String projectNo;

    private String projectName;

    private String projectTheme;

    private String projectDesc;

    private String projectSponsor;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String userId;

    private String filePath;

    private Integer isMaker;
}
