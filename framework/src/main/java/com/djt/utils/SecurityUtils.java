package com.djt.utils;

import com.djt.domain.entity.LoginUser;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
public class SecurityUtils
{

    /**
     * 获取用户
     **/
    public static LoginUser getLoginUser()
    {
        log.info("开始获取用户:");
        //次authentication为null
        Authentication authentication = getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        log.info("获取用户:"+loginUser);
        return loginUser;
    }

    /**
     * 获取Authentication
     */
    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    //判断当前登录的用户是否是管理员
    public static Boolean isAdmin(){
        Long id = getLoginUser().getUser().getId();
        return id != null && id.equals(1L);
    }

    public static Long getUserId() {
        return getLoginUser().getUser().getId();
    }
}