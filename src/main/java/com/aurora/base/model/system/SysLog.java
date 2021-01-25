package com.aurora.base.model.system;

import com.aurora.base.model.PageModel;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 日志实体
 * @author PHQ
 * @create 2020-05-01 23:18
 **/
@Data
@Accessors(chain = true)
@TableName("SYS_LOG")
@Builder
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class SysLog extends PageModel implements Serializable {

        @TableId(value = "LOG_ID", type = IdType.AUTO)
        private int id;

        @TableField("LOG_USER")
        private String logUser;

        @TableField("LOG_ROLE")
        private String logRole;

        @TableField("LOG_MODULE")
        private String logModule;

        @TableField("LOG_METHOD")
        private String logMothod;

        @TableField("LOG_URL")
        private String logUrl;

        @TableField("LOG_IP")
        private String logIp;

        @TableField("LOG_DESC")
        private String logDesc;

        @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")//页面写入数据库时格式化
        @JsonFormat(timezone = "GMT+8",pattern ="yyyy-MM-dd HH:mm:ss")
        @TableField("LOG_CREATE_TIME")
        private Timestamp logCreateTime;

        @TableField("LOG_TYPE")
        private int logType = 0;


}
