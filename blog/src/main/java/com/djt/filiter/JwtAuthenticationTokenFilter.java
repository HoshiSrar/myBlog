package com.djt.filiter;

import com.alibaba.fastjson.JSON;
import com.djt.domain.ResponseResult;
import com.djt.domain.entity.LoginUser;
import com.djt.enums.AppHttpCodeEnum;
import com.djt.utils.JwtUtil;
import com.djt.utils.RedisCache;
import com.djt.utils.SecurityUtils;
import com.djt.utils.WebUtils;
import jdk.nashorn.internal.runtime.logging.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Logger
/**
 * 过滤器
 */
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Resource
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException{

        //获取请求头中的token
        String token = request.getHeader("token");
        logger.info("token:"+token);
        List<String> cookie1=null;
        SecurityContext Context=null;
        Authentication UntilContext=null;

        try {
            cookie1= Arrays.stream(request.getCookies()).map(cookie -> cookie.getName()+":"+cookie.getValue()).collect(Collectors.toList());
            Context = SecurityContextHolder.getContext();
            UntilContext = SecurityUtils.getAuthentication();
        }catch (Exception ignored){
            logger.info("Cookie:"+ cookie1);
            logger.info("自己获取："+Context);
            logger.info("工具包获取："+UntilContext);
        }
        if(!StringUtils.hasText(token)) {
            //说明该接口尚未登录，直接放行
            filterChain.doFilter(request, response) ;
            return;
        }
        //解析token获取id
        String id=null;
        try {
            id = JwtUtil.parseJWT(token).getSubject();
        } catch (Exception e) { //token超时或非法
            e.printStackTrace();
            //提示前段需要重新登录,调用工具类返回 封装后转为的JSON 信息
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //获取redis中的LoginUser信息
        LoginUser loginUser = redisCache.getCacheObject("bloglogin:"+id);
        //如果获取为空，说明登录过期，提示重新登录
        if(Objects.isNull(loginUser)) {
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
            WebUtils.renderString(response, JSON.toJSONString(result));
            return;
        }
        //存入Security中的SecurityContextHolder中
        UsernamePasswordAuthenticationToken token1 = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(token1);




//        logger.info("封装成 authentication的token信息："+token1);
        filterChain.doFilter(request,response);
    }
}
