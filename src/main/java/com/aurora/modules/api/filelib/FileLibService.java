package com.aurora.modules.api.filelib;

import com.aurora.base.common.model.ResultModel;
import com.aurora.modules.model.filelib.Filelib;

/**
 *  文件库业务接口
 * @author :PHQ
 * @date：2021/3/18
 **/
public interface FileLibService {

    /**
     * 获取文件列表
     * @param filelib
     * @return
     */
    public ResultModel getFileLibList(Filelib filelib);

    /**
     * 文件保存
     * @param filelib
     * @return
     */
    public ResultModel saveFileLib(Filelib filelib);

    /**
     * 文件删除
     * @param fileId
     * @return
     */
    public  ResultModel deleteFileLibById(Integer fileId);

    /**
     * 文件更新
     * @param filelib
     * @return
     */
    public  ResultModel editFileLib(Filelib filelib);



}
