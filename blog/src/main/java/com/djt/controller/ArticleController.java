package com.djt.controller;


import com.alibaba.fastjson.JSON;
import com.djt.annotation.SystemLog;
import com.djt.domain.ResponseResult;
import com.djt.service.ArticleService;
import com.djt.utils.WebUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

@RestController
//@RequestMapping("/api/article")
@RequestMapping("/article")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    // 查询热点文章
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){
        ResponseResult result = articleService.getHotArticleList();
        return result;
    }

    //查询首页的置顶和示例文章
    @GetMapping("/articleList")
    @SystemLog(BusinessName = "查询首页置顶和示例文章")
    public ResponseResult articleList(Integer pageNum,Integer pageSize,Long categoryId){
        return articleService.articleList(pageNum,pageSize,categoryId);
    }

    //根据id查找文章详情
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }
    //每次点击博客更新博客访问数据
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}
