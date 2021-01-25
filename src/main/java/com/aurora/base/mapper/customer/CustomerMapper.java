package com.aurora.base.mapper.customer;

import com.aurora.base.model.auth.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
/**
 * 客户业务mapper 操作类
 * @author :PHQ
 * @date：2020/7/7
 **/
@Repository
public interface CustomerMapper extends BaseMapper<User> {


}
