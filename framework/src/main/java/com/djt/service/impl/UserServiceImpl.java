package com.djt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djt.domain.ResponseResult;
import com.djt.domain.entity.User;
import com.djt.domain.vo.UserInfoVo;
import com.djt.enums.AppHttpCodeEnum;
import com.djt.exception.SystemException;
import com.djt.mapper.UserMapper;
import com.djt.service.UserService;
import com.djt.utils.BeanCopyUtils;
import com.djt.utils.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-03-06 18:25:04
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    /**
     * 获取登录用户信息
     */
    public ResponseResult getUserInfo() {
        User user = getById(SecurityUtils.getUserId());
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    //    用户信息更新
    public ResponseResult updateUserInfo(User user) {
        LambdaUpdateWrapper<User> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(User::getId,user.getId());
        updateWrapper.set(User::getAvatar,user.getAvatar());
        updateWrapper.set(User::getEmail,user.getEmail());
        updateWrapper.set(User::getId,user.getId());
        updateWrapper.set(User::getNickName, user.getNickName());
        updateWrapper.set(User::getSex, user.getSex());
        updateById(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName()) ){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);}
        if(!StringUtils.hasText(user.getPassword()) ){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);}
        if(!StringUtils.hasText(user.getEmail()) ){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);}
        if(!StringUtils.hasText(user.getNickName()) ){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);}

        //验证数据库中用户名是否存在
        if(UserNameExit(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);}
        //验证数据库中昵称是否存在
        if(UserNickNameExit(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);}
        //对密码进行加密
        String encodePassWord = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassWord);
        //储存数据库中
        save(user);
        return ResponseResult.okResult();
    }

    /**
     * 查询数据库中用户名是否存在，存在返回true
     * @param userName
     * @return boolean
     */
    private boolean UserNameExit(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);
        //查询数据库中用户名相同的条数
        return count(queryWrapper)>0;
    }
    /**
     * 查询数据库中昵称是否存在，存在返回true
     * @param nickName
     * @return boolean
     */
    private boolean UserNickNameExit(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);
        //查询数据库中用户名相同的条数
        return count(queryWrapper)>0;
    }
}
