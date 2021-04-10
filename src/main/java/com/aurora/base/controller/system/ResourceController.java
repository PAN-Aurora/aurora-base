package com.aurora.base.controller.system;

import com.aurora.base.api.system.ResourceService;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.config.annotation.SystemLog;
import com.aurora.base.model.system.Resource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单资源控制类
 * @author :PHQ
 * @date：2020/5/15
 **/
@Api(tags = "资源管理",description="资源管理")
@RestController
@RequestMapping("/api/resource")
public class ResourceController {

     @Autowired
     private ResourceService resourceService;


    /**
     * 资源列表
     * @param resource
     * @return
     */
    @GetMapping(value = "/getResourceList")
    @SystemLog(module="资源管理模块",methods="获取资源集合",url="/api/resource/getResourceList", desc="获取资源集合")
    @ApiOperation(value = "获取资源集合",notes = "获取资源集合分页列表数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resource", value = "资源实体对象 resource", required = true, dataType = "com.aurora.model.auth.Resource")
    })
    public ResultModel getResourceList(Resource resource){
        return resourceService.getResourceList(resource);
    }
     /**
     * 资源树
     * @param resource
     * @return
     */
    @PostMapping(value = "/getResourceListTree")
    @SystemLog(module="资源管理模块",methods="获取资源树",url="/api/resource/getResourceListTree", desc="获取资源树")
    @ApiOperation(value = "资源树",notes = "获取资源树数据",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "resource", value = "资源实体对象 resource", required = true, dataType = "com.aurora.model.auth.Resource")
    })
    public ResultModel getResourceListTree(Resource resource){
        return resourceService.getResourceListTree(resource);
    }
}
