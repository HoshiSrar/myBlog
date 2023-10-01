package com.djt.controller;

import com.djt.annotation.SystemLog;
import com.djt.domain.ResponseResult;
import com.djt.domain.entity.User;
import com.djt.enums.AppHttpCodeEnum;
import com.djt.exception.SystemException;
import com.djt.service.UserService;
import com.djt.utils.WebUtils;
import org.apache.http.HttpResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService service;

    @GetMapping("/userInfo")
    public ResponseResult getUserInfo(){
//        WebUtils.renderString(response,"这里是获取用户信息");
        return service.getUserInfo();
    }
    @PutMapping("/userInfo")
    @SystemLog(BusinessName = "更新用户信息接口")
    public ResponseResult updateUserInfo(@RequestBody User user){//参数在请求体中，用RequestBody标识，用实体类来接受参数
        return service.updateUserInfo(user);
    }
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user){
        return service.register(user);
    }

}
