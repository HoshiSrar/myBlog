package com.djt.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.djt.domain.ResponseResult;
import com.djt.domain.dto.ArticleDto;
import com.djt.domain.entity.Article;
import com.djt.domain.vo.ArticleVo;
import com.djt.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    /**
     * 查询热门文章，封装为ResponseResult返回
     */
    ResponseResult getHotArticleList();


    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult addArticle(ArticleDto article);

    PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize);

    ArticleVo getInfo(Long id);

    void removeById(Long id);

    void edit(ArticleDto article);
}
