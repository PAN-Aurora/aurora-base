package com.aurora.modules.service.filelib;

import com.alibaba.fastjson.JSON;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.common.util.StringUtil;
import com.aurora.modules.api.filelib.FileLibService;
import com.aurora.modules.mapper.filelib.FileLibMapper;
import com.aurora.modules.model.filelib.Filelib;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *  文件库业务实现
 * @author :PHQ
 * @date：2021/3/18
 **/
@Service
public class FileLibServiceImpl implements FileLibService {

    private final static Logger logger = LoggerFactory.getLogger(FileLibServiceImpl.class);

    @Autowired
    FileLibMapper fileLibMapper;

    @Override
    public ResultModel getFileLibList(Filelib filelib) {
        //分页参数
        Page<Filelib> page = new Page<>(filelib.getCurrent(), filelib.getLimit());
        QueryWrapper<Filelib> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtil.isNotBlank(filelib.getFileName())){
            queryWrapper.eq("file_name",filelib.getFileName());
        }
        IPage<Filelib> userIPage =  fileLibMapper.selectPage(page,queryWrapper);

        logger.info(userIPage.getTotal()+"");
        logger.info(JSON.toJSONString(userIPage.getRecords()));
        return ResultModel.successPage(userIPage.getRecords(),userIPage.getTotal());
    }

    @Override
    public ResultModel saveFileLib(Filelib filelib) {
        fileLibMapper.insert(filelib);
        return ResultModel.success(ResultCode.SUCCESS.getCode(),"保存成功！");
    }

    @Override
    public ResultModel deleteFileLibById(Integer fileId) {
        fileLibMapper.deleteById(fileId);
        return ResultModel.success(ResultCode.SUCCESS.getCode(),"删除成功！");
    }

    @Override
    public ResultModel editFileLib(Filelib filelib) {
        fileLibMapper.updateById(filelib);

        return ResultModel.success(ResultCode.SUCCESS.getCode(),"更新成功！");
    }
}
