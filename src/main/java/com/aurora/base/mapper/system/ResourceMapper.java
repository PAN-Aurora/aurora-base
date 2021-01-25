package com.aurora.base.mapper.system;

import com.aurora.base.model.system.Resource;
import com.aurora.base.model.system.vo.MenuTree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 资源mapper
 * @author :PHQ
 * @date：2020/5/8
 **/
@Repository
public interface ResourceMapper extends BaseMapper<Resource> {

    //获取上有所菜单权限
    public List<Resource> getResourceListByRoleId(@Param("roleId") long roleId);
    //获取所有按钮权限
    public List<Resource> getResourceListByType(@Param("roleId") long roleId);
    //通过父节点获取菜单数据
    public List<MenuTree> getResourceListByParentId(@Param("pid") int pid);

}
