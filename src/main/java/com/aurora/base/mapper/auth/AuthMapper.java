package com.aurora.base.mapper.auth;

import com.aurora.base.model.auth.User;
import com.aurora.base.model.system.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 *  登录相关操作的mapper
 * @author PHQ
 * @create 2020-05-03 11:39
 **/
@Repository
//@Mapper
public interface AuthMapper  extends BaseMapper<User> {
    /**
     * 通过用户名 查询用户信息
     * @param user
     * @return
     */
    public User findByUsername(User user);

    /**
     * 通过用户id 查询对象的角色列表
     * @param user
     * @return
     */
    public Role findRoleByUserId(User user);


    /**
     * 创建新用户
     * @param user
     */
    int insertUser(User user);

    /**
     * 创建用户角色
     * @param userId
     * @param roleId
     * @return
     */
    int insertRole(@Param("userId") long userId, @Param("roleId") long roleId);

    /**
     * 删除用户对应角色
     * @param userId
     * @return
     */
    int deleteUserRoleById(@Param("userId") long userId);

    /**
     * 根据角色id查找角色
     * @param roleId
     * @return
     */
    Role findRoleById(@Param("roleId") long roleId);

}
