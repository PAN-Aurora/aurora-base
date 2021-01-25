package com.aurora.base.service.system;

import com.alibaba.fastjson.JSON;
import com.aurora.base.api.system.SysLogService;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.mapper.system.SysLogMapper;
import com.aurora.base.model.system.SysLog;
import com.aurora.base.model.system.vo.SysLogVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author PHQ
 * @create 2020-05-01 23:45
 **/
@Service
public class SysLogServiceImpl implements SysLogService {

    private final static Logger logger = LoggerFactory.getLogger(SysLogServiceImpl.class);


    @Autowired
    private SysLogMapper sysLogMapper;

    /**
     * 系统日志保存
     * @param sysLog
     * @return
     */
    public int saveLog(SysLog sysLog){
        return sysLogMapper.insert(sysLog);
    }


    /**
     * 获取日志列表
     * @param sysLog
     * @return
     */
    public ResultModel getLogList(SysLogVo sysLog){
        //分页参数
        Page<SysLog> page = new Page<>(sysLog.getCurrent(), sysLog.getLimit());
        QueryWrapper<SysLog> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtils.isNotBlank(sysLog.getLogUser())){
            queryWrapper.like("LOG_USER",sysLog.getLogUser());
        }
        if(StringUtils.isNotBlank(sysLog.getLogModule())){
            queryWrapper.like("LOG_MODULE",sysLog.getLogModule());
        }
        if(sysLog.getLogType()>0){
            queryWrapper.like("LOG_TYPE",sysLog.getLogType());
        }
        if(StringUtils.isNotBlank(sysLog.getStartTime()) && StringUtils.isNotBlank(sysLog.getEndTime())){
            queryWrapper.between("LOG_CREATE_TIME",sysLog.getStartTime(),sysLog.getEndTime());
        }
        //根据时间排序
        queryWrapper.orderByDesc("LOG_CREATE_TIME");


        IPage<SysLog> userIPage =  sysLogMapper.selectPage(page,queryWrapper);
        List<SysLog> list =  userIPage.getRecords();

        logger.info(userIPage.getTotal()+"");
        logger.info(JSON.toJSONString(list));
        return ResultModel.successPage(list,userIPage.getTotal());

    }

}
