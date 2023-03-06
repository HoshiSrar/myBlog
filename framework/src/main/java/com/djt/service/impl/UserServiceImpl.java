package com.djt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djt.domain.entity.User;
import com.djt.mapper.UserMapper;
import com.djt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-06 18:25:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
