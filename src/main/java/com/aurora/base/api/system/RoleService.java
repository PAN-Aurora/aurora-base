package com.aurora.base.api.system;


import com.aurora.base.common.model.ResultModel;
import com.aurora.base.model.system.Role;

/**
 *
 * @author PHQ
 * @create 2020-05-01 22:44
 **/
public interface RoleService {

    //获取角色列表
    public ResultModel getRoleList(Role role);

    //增加角色
    public ResultModel  insertRole(Role role);
    //更新角色
    public ResultModel  updateRole(Role role);
    //刪除角色
    public ResultModel  deleteRole(Role role);
}
