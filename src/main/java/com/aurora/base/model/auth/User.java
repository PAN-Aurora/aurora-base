package com.aurora.base.model.auth;

import com.alibaba.fastjson.annotation.JSONField;
import com.aurora.base.model.PageModel;
import com.aurora.base.model.system.Resource;
import com.aurora.base.model.system.Role;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *  用户model
 * @author PHQ
 * @create 2020-05-02 20:45
 **/
@Data
@EqualsAndHashCode(callSuper=false)
@Accessors(chain = true)
@TableName(value = "SYS_USER",autoResultMap=true)
public class User extends PageModel implements UserDetails  {

    @TableId(value = "ID", type = IdType.AUTO)
    private long id;

    @TableField("USERNAME")
    private String username;

    @TableField("REAL_NAME")
    private String realName;

    @TableField("PASSWORD")
    private String password;

    @TableField("SEX")
    private int sex;

    @TableField("AGE")
    private int age;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Role role;

    @TableField(exist = false)
    private List<Resource> menuList;

    @TableField(exist = false)
    private List<Resource> resourceList;

    @TableField(exist = false)
    @JSONField(serialize = false)
    private Date lastPasswordResetDate;

    public User() {
    }

    public User(long id) {
        this.id = id;
    }

    public User(
            long id,
            String username,
            Role role,
            String password) {
                this.id = id;
                this.username = username;
                this.password = password;
                this.role = role;
    }

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public User(long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }
    //返回分配给用户的角色列表
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        if(role!=null){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    /**
     * 账户是否未过期
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     *  账户是否未锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    /**
     * 密码是否未过期
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /** 账户是否激活
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

}
