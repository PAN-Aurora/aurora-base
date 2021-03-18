package com.aurora.modules.controller.filelib;

import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.common.util.StringUtil;
import com.aurora.base.config.annotation.SystemLog;
import com.aurora.modules.api.filelib.FileLibService;
import com.aurora.modules.model.filelib.Filelib;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 *  文件库管理
 * @author :PHQ
 * @date：2021/3/18
 **/
@RestController
@RequestMapping("/filelib")
public class FileLibController {

    @Autowired
    FileLibService  fileLibService;

    @Value("${aurora.file.root.path}")
    String  fileRootPath;


    /**
     * 获取用户列表
     * @param filelib
     * @return
     */
    @GetMapping(value = "/getFileLibList")
    @SystemLog(module="文件管理模块",methods="获取文件列表",url="/filelib/getFileLibList", desc="获取文件分页列表数据")
    @ApiOperation(value = "文件管理模块",notes = "获取文件列表",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filelib", value = "文件实体对象", required = true, dataType = "com.aurora.modules.model.filelib.Filelib")
    })
    public ResultModel getFileLibList(Filelib filelib){
        return fileLibService.getFileLibList(filelib);
    }


    /**
     * 保存文件
     * @param filelib
     * @return
     */
    @PostMapping(value = "/saveFileLib")
    @SystemLog(module="文件管理模块",methods="保存文件",url="/filelib/saveFileLib", desc="保存文件")
    @ApiOperation(value = "保存文件",notes = "保存文件",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filelib", value = "文件实体对象", required = true, dataType = "com.aurora.modules.model.filelib.Filelib")
    })
    public ResultModel saveFileLib(@RequestParam("files")MultipartFile[] files , @RequestBody Filelib filelib){
        if(files != null && files.length>0){
            //对文件进行处理 保存到具体目录并将路径存放到实体内
            // 多文件上传
            for (MultipartFile file : files){
                // 上传简单文件名
                String originalFilename = file.getOriginalFilename();
                // 存储路径
                String filePath = new StringBuilder(fileRootPath)
                        .append(System.currentTimeMillis())
                        .append(originalFilename)
                        .toString();
                try {
                    // 保存文件
                    file.transferTo(new File(filePath));


                    if (originalFilename.endsWith(".doc")||originalFilename.endsWith("docx")) {
                        filelib.setFileType("Word");
                    } else if (originalFilename.endsWith("pdf")) {
                        filelib.setFileType("PDF");
                    }
                    //文件信息入库
                    filelib.setFilePath(filePath);
                    filelib.setFileName(originalFilename);
                    filelib.setCreateTime(new Date());
                    fileLibService.saveFileLib(filelib);

                    //将文件内容解析存入es

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return ResultModel.success(ResultCode.SUCCESS.getCode(),"保存成功！");
        }
        return ResultModel.failure(ResultCode.BAD_PARAMS);
    }


    /**
     * 删除文件
     * @param filelib
     * @return
     */
    @PostMapping(value = "/deleteFileLibById")
    @SystemLog(module="用户管理模块",methods="删除文件",url="/filelib/deleteFileLibById", desc="删除文件")
    @ApiOperation(value = "删除文件",notes = "删除文件",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "filelib", value = "文件实体对象", required = true, dataType = "com.aurora.modules.model.filelib.Filelib")
    })
    public ResultModel deleteFileLibById(@RequestBody Filelib filelib){
        if(filelib.getFileId() != null ){
            //针对文件进行本地清除
            return fileLibService.deleteFileLibById(filelib.getFileId());
        }
        return ResultModel.failure(ResultCode.BAD_PARAMS);
    }

}
