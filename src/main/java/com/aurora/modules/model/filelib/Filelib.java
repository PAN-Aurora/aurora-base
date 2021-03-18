package com.aurora.modules.model.filelib;

import com.aurora.base.model.PageModel;
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
@TableName(value = "TBL_FILELIB",autoResultMap=true)
public class Filelib extends PageModel {

    @TableId(value = "FILE_ID",type = IdType.AUTO)
    private Integer fileId;

    private String fileNo;

    private String fileName;

    private Integer fileVersion;

    private String fileCategory;

    private String filePath;

    private String fileType;

    private String fileDesc;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Integer userId;

}