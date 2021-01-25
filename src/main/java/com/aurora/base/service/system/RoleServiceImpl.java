package com.aurora.base.service.system;

import com.alibaba.fastjson.JSON;
import com.aurora.base.api.system.RoleService;
import com.aurora.base.common.model.ResultCode;
import com.aurora.base.common.model.ResultModel;
import com.aurora.base.common.util.StringUtil;
import com.aurora.base.mapper.system.ResourceMapper;
import com.aurora.base.mapper.system.RoleMapper;
import com.aurora.base.model.system.Resource;
import com.aurora.base.model.system.Role;
import com.aurora.base.model.system.RoleResource;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *  角色业务实现
 *     针对角色新增，修改，删除 授权
 * @author PHQ
 * @create 2020-05-01 22:45
 **/
@Service
public class RoleServiceImpl implements RoleService {
    private final static Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private ResourceMapper resourceMapper;


    /**
     * 查询用户列表
     * @param roleVo
     * @return
     */
    public ResultModel getRoleList(Role roleVo){
        //分页参数
        Page<Role> page = new Page<>(roleVo.getCurrent(), roleVo.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtil.isNotBlank(roleVo.getName())){
            queryWrapper.eq("name",roleVo.getName());
        }
        IPage<Role> userIPage =  roleMapper.selectPage(page,queryWrapper);
        List<Role> roleList =  userIPage.getRecords();

        //遍历角色获取菜单资源
        roleList.forEach(role -> {
            List<Resource> resourceList = resourceMapper.getResourceListByRoleId(role.getId());
            role.setRosourceList(resourceList);
        });

        logger.info(userIPage.getTotal()+"");
        logger.info(JSON.toJSONString(roleList));
        return ResultModel.successPage(roleList,userIPage.getTotal());

    }

    public  Integer   getRoleCount(Role role){
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtil.isNotBlank(role.getName())){
            queryWrapper.eq("name",role.getName());
            return  roleMapper.selectCount(queryWrapper);
        }
        return  0;
    }

    /**
     * 增加角色
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResultModel  insertRole(Role role){

        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        //查询参数
        if(StringUtil.isNotBlank(role.getName())){
            queryWrapper.eq("name",role.getName());

        }
         if(roleMapper.selectCount(queryWrapper)  > 0){
             return ResultModel.success(ResultCode.BAD_PARAMS.getCode(),"角色名称重复！");
         }
          //保存角色信息
          roleMapper.insertRole(role);
          //先删除角色对应映射关系
           roleMapper.deleteRoleResuorceById(role.getId());
          //增加资源和角色映射
           if(role.getRosourceList()!=null && role.getRosourceList().size()>0){
               List<Resource>  rosourceList =  role.getRosourceList();

               List<RoleResource> roleResourceList = new ArrayList<RoleResource>();
               rosourceList.forEach(resource -> {
                   roleResourceList.add(RoleResource.builder()
                           .roleId(role.getId())
                           .resourceId(resource.getId())
                           .build());
               });
               //保存角色资源映射
               roleMapper.insertBatchRoleResuorce(roleResourceList);
           }
          return ResultModel.success(ResultCode.SUCCESS.getCode(),"保存角色成功！");
    }
    /**
     * 更新角色
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultModel updateRole(Role role) {
        //更新角色
        roleMapper.updateById(role);
        //先删除角色对应映射关系
        roleMapper.deleteRoleResuorceById(role.getId());

        if(role.getRosourceList()!=null && role.getRosourceList().size()>0){
            List<Resource>  rosourceList =  role.getRosourceList();

            List<RoleResource> roleResourceList = new ArrayList<RoleResource>();
            rosourceList.forEach(resource -> {
                roleResourceList.add(RoleResource.builder()
                        .roleId(role.getId())
                        .resourceId(resource.getId())
                        .build());
            });
            //保存角色资源映射
            roleMapper.insertBatchRoleResuorce(roleResourceList);
        }
        return ResultModel.success(ResultCode.SUCCESS.getCode(),"更新角色成功！");
    }

    /**
     * 删除角色
     * @param role
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultModel deleteRole(Role role) {

        int[] ids =  role.getIds();
        for(int i=0;i<ids.length;i++){
            //先刪除角色对应资源
            roleMapper.deleteRoleResuorceById(ids[i]);
            //然后删除角色
            roleMapper.deleteById(ids[i]);
        }

        return ResultModel.success(ResultCode.SUCCESS.getCode(),"刪除角色成功！");
    }
}
