package com.aurora.base.service.system;

import com.aurora.base.api.system.ResourceService;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.mapper.system.ResourceMapper;
import com.aurora.base.model.system.Resource;
import com.aurora.base.model.system.vo.MenuTree;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源管理
 * @author :PHQ
 * @date：2020/5/15
 **/
@Service
public class ResourceServiceImpl  implements ResourceService {

    @Autowired
    private ResourceMapper resourceMapper;

    /**
     * 资源列表
     * @return
     */
    public ResultModel getResourceList(Resource resource){
        //分页参数
        Page<Resource> page = new Page<>(resource.getCurrent(), resource.getLimit());
        QueryWrapper<Resource> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtils.isNotBlank(resource.getName())){
            queryWrapper.eq("name",resource.getName());
        }
        IPage<Resource> userIPage =  resourceMapper.selectPage(page,queryWrapper);
        List<Resource> roleList =  userIPage.getRecords();

        return ResultModel.successPage(roleList,userIPage.getTotal());

    }
    /**
     * 查询资源树
     * @return
     */
    public ResultModel getResourceListTree(Resource resource){

         //一级菜单
        List<MenuTree>  menuTreeList =   resourceMapper.getResourceListByParentId(0);
        menuTreeList.forEach(menu ->{
            //二级
            List<MenuTree>  childList =  resourceMapper.getResourceListByParentId(menu.getIdKey());
            menu.setChildren(childList);
            //三级
            if(childList !=null && childList.size()>0){
                childList.forEach(child ->{
                    List<MenuTree>  threeList =  resourceMapper.getResourceListByParentId(child.getIdKey());
                    child.setChildren(threeList);
                });
            }
        });
        return ResultModel.successData(ResultCode.SUCCESS,menuTreeList);

    }


}
