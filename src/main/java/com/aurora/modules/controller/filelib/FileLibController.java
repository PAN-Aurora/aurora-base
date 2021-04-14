package com.aurora.modules.controller.filelib;

import com.alibaba.fastjson.JSONObject;
import com.aurora.base.common.model.IndexModel;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.common.service.EsService;
import com.aurora.base.common.util.FileUpload;
import com.aurora.base.common.util.PdfUtil;
import com.aurora.base.config.annotation.SystemLog;
import com.aurora.modules.api.filelib.FileLibService;
import com.aurora.modules.consts.IndexConstant;
import com.aurora.modules.model.filelib.Filelib;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  文件库管理
 * @author :PHQ
 * @date：2021/3/18
 **/
@Api(value = "文件库管理",tags = "文件库管理",description="文件库管理",position=0)
@RestController
@RequestMapping("/api/filelib")
public class FileLibController  {

    private final static Logger logger = LoggerFactory.getLogger(FileLibController.class);


    @Autowired
    FileLibService  fileLibService;

    @Value("${system.file.root.path}")
    String  fileRootPath;

    @Value("${system.file.size}")
    double  fileSize;

    @Value("${system.es.file_index}")
    String  fileIndex;

    @Value("${system.es.file_type}")
    String  fileType;

    @Autowired
    EsService esService;

    /**
     * 获取用户列表
     * @param filelib
     * @return
     */
    @GetMapping(value = "/getFileLibList")
    @SystemLog(module="获取文件列表",methods="获取文件列表",url="/filelib/getFileLibList", desc="获取文件分页列表数据")
    @ApiOperation(value = "获取文件列表",notes = "获取文件列表",httpMethod = "GET")
    @ApiImplicitParams({
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
    })
    public ResultModel saveFileLib(@RequestParam("files")MultipartFile[] files ,Filelib filelib){

        if(files != null && files.length>0){
            //对文件进行处理 保存到具体目录并将路径存放到实体内
            // 多文件上传
            for (MultipartFile file : files){
                //获取文件大小 M
                String size  = String.valueOf(file.getSize());
                double  fileLength =  Double.valueOf(size)/1024/1024;
                //文件太大就提示不让保存
                if(fileLength > fileSize){

                    return ResultModel.success(ResultCode.SERVER_ERROR.getCode(),"文件大小超过规定大小！");

                }
                // 上传简单文件名
                String originalFilename = file.getOriginalFilename();
                //获取文件后缀
                String suffix =  file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
                // 存储文件夹
                String dirName = new StringBuilder()
                        .append(format.format(new Date()))
                        .append("/").toString();
                //存储的文件名称
                StringBuilder fileName = new StringBuilder(System.currentTimeMillis()+"");

                //文件夹路径
                String dirPath= new StringBuilder(fileRootPath).append(dirName).toString();

                //文件路径
                String filePath = dirPath + fileName.append(suffix).toString();

                try {

                    //需要保存的文件
                    File saveFile =  FileUpload.mkdirsmy(dirPath,fileName.append(suffix).toString());
                    // 保存文件
                    file.transferTo(saveFile);

                    //文件信息入库
                    filelib.setFilePath(filePath);
                    filelib.setFileName(originalFilename);
                    filelib.setCreateTime(new Date());
                    fileLibService.saveFileLib(filelib);

                    //针对word 文件 doc和 docx文件换成pdf 用于预览
                    String pdfPath = "";
                    if (originalFilename.endsWith(".doc")|| originalFilename.endsWith("docx")) {
                        pdfPath =  dirPath + fileName.append(".pdf").toString();
                        PdfUtil.doc2pdf(filePath,pdfPath);

                    }  else if (originalFilename.endsWith("pdf")) {
                        pdfPath =  filePath;
                    }

                    if(filelib.getFileId()>0){
                        //将文件内容解析存入es
                        StringBuilder  bulider =  FileUpload.getTextFromWordOrPdf(filePath);
                        //调用elasticsearch保存
                        if(bulider.toString().replaceAll("\r\t", "").length()>0){

                            java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
                            //调用elasticsearch保存
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put(IndexConstant.FILE_ID, filelib.getFileId());
                            jsonObject.put(IndexConstant.FILE_NAME, file.getOriginalFilename());
                            jsonObject.put(IndexConstant.FILE_NO, filelib.getFileNo());
                            jsonObject.put(IndexConstant.FILE_SIZE, df.format(fileLength));
                            jsonObject.put(IndexConstant.FILE_TITLE, fileName);
                            jsonObject.put(IndexConstant.FILE_TYPE, filelib.getFileType());
                            jsonObject.put(IndexConstant.FILE_CATEGORY, filelib.getFileCategory());
                            //关键词 取值于文章中 todo
                            jsonObject.put(IndexConstant.FILE_KEYWORDS, filelib.getFileCategory());
                            jsonObject.put(IndexConstant.FILE_URL, filePath);
                            jsonObject.put(IndexConstant.FILE_URL_PDF, pdfPath);
                            jsonObject.put(IndexConstant.FILE_CONTENT, bulider.toString().replaceAll("\r\t", ""));

                            DateFormat date = DateFormat.getDateTimeInstance();
                            jsonObject.put(IndexConstant.CREATED_TIME, date.format(new Date()));

                            esService.addData(jsonObject, fileIndex, fileType);

                        }
                    }else{

                        logger.info("##########数据未存入elasticsearch中");
                    }


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
    })
    public ResultModel deleteFileLibById(@RequestBody Filelib filelib){
        if(filelib.getIds()!= null  && filelib.getIds().length !=0){
            //删除文件
            List<IndexModel> modelList = new ArrayList<IndexModel>();
            for(int id: filelib.getIds()){
                fileLibService.deleteFileLibById(id);
                esService.deleteDataById(fileIndex,fileType,String.valueOf(id));
                IndexModel model = IndexModel.builder()
                        .searchType(IndexConstant.TERM_QUERY)
                        .fieldName(IndexConstant.FILE_ID).fieldValue(String.valueOf(id)).build();
                        modelList.add(model);
            }
            esService.deleteDataByQuery(fileIndex,modelList);
            //针对文件进行本地清除
            return ResultModel.success(ResultCode.SUCCESS.getCode(),"删除成功！");
        }
        return ResultModel.failure(ResultCode.BAD_PARAMS);
    }

}
