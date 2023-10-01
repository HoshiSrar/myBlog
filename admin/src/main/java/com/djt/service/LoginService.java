package com.djt.service;

import com.djt.domain.ResponseResult;
import com.djt.domain.entity.User;

import javax.servlet.http.HttpServletResponse;

public interface LoginService {
    ResponseResult login(User user, HttpServletResponse response);
    ResponseResult logout();

}
