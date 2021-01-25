package com.aurora.base.service.auth;

import com.aurora.base.mapper.auth.AuthMapper;
import com.aurora.base.mapper.system.ResourceMapper;
import com.aurora.base.model.auth.User;
import com.aurora.base.model.system.Resource;
import com.aurora.base.model.system.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 登录权限校验 默认 security 自动调用
 * @author PHQ
 * @create 2020-05-03 11:37
 **/
@Component(value="CustomUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private final AuthMapper authMapper;

    @Autowired
    private final ResourceMapper resourceMapper;

    public CustomUserDetailsService(AuthMapper authMapper,ResourceMapper resourceMapper) {
        this.authMapper = authMapper;
        this.resourceMapper = resourceMapper;
    }

    @Override
    public User loadUserByUsername(String name) throws UsernameNotFoundException {
        User userLogin = new User();
        userLogin.setUsername(name);
        //查询登录的用户
        User user = authMapper.findByUsername(userLogin);
        if (user == null) {
             throw new UsernameNotFoundException(String.format("没有发现用户： '%s'.", name));
        }
        //查询用户登录角色
        Role role = authMapper.findRoleByUserId(user);
        user.setRole(role);

        if(role!= null){
            //根据角色查询角色资源
            List<Resource>  menuList =   resourceMapper.getResourceListByRoleId(role.getId());
            user.setMenuList(menuList);

            List<Resource>  resourceList =   resourceMapper.getResourceListByType(role.getId());
            user.setResourceList(resourceList);
        }

//        if(role!= null){
//            //根据角色查询角色资源
//            List<Resource>  menuList =   resourceMapper.getResourceListByParentId(role.getId(),0);
//            menuList.forEach(menu ->{
//                //二级
//                List<Resource>  childList =  resourceMapper.getResourceListByParentId(role.getId(),menu.getId());
//                menu.setChildResourceList(childList);
//
//                //三级
//                if(childList !=null && childList.size()>0){
//                    childList.forEach(child ->{
//                        List<Resource>  threeList =  resourceMapper.getResourceListByParentId(role.getId(),child.getId());
//                        child.setChildResourceList(threeList);
//                    });
//                }
//            });
//            user.setResourceList(menuList);
//        }
        return user;
    }
}
