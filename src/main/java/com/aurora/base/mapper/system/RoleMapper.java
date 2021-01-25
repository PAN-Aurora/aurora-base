package com.aurora.base.mapper.system;

import com.aurora.base.model.system.Role;
import com.aurora.base.model.system.RoleResource;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * role mapper
 * @author PHQUser
 * @create 2020-05-01 22:14
 **/
@Repository
public interface RoleMapper extends BaseMapper<Role> {
    //增加角色信息
    public int  insertRole(Role roleVo);
    //删除角色对应资源
    public int  deleteRoleResuorceById(@Param("roleId") int roleId);
    //批量增加角色资源
    public int  insertBatchRoleResuorce(List<RoleResource> list);

}
