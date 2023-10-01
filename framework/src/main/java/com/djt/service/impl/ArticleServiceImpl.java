package com.djt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.djt.constants.SystemConstants;
import com.djt.domain.ResponseResult;
import com.djt.domain.dto.ArticleDto;
import com.djt.domain.entity.Article;
import com.djt.domain.entity.ArticleTag;
import com.djt.domain.entity.Category;
import com.djt.domain.entity.Tag;
import com.djt.domain.vo.*;
import com.djt.mapper.ArticleMapper;
import com.djt.service.ArticleService;
import com.djt.service.ArticleTagService;
import com.djt.service.TagService;
import com.djt.utils.BeanCopyUtils;
import com.djt.utils.RedisCache;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.method.annotation.SessionAttributesHandler;

import javax.annotation.Resource;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 功能实现。
 *  继承MybatisPlus，实现上层接口，获得部分sql
 * */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Resource
    RedisCache redisCache;
    @Resource
    private CategoryServiceImpl categoryService;
    @Resource
    private ArticleTagService articleTagService;
    @Resource
    private TagService tagService;
    /**
     * 获取热门文章列表，响应体内含热门文章列表
     * */
    @Override
    public ResponseResult getHotArticleList() {
        //查询热门文章，封装为ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        //必须是正式文章不是草稿
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);//将表中的status值和0匹配
        //按照浏览量排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询十条
        Page<Article> page = new Page<>(1,10);
        //将分页和Wrapper 封装
        page(page,queryWrapper);
        //获得当前页数据
        List<Article> articles = page.getRecords();
        //将viewCount更换成redis中的值
        articles.forEach(article ->
                article.setViewCount(Long.valueOf(
                        redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_KEY,
                                article.getId().toString()).toString()
                )));

        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        return ResponseResult.okResult(vs);
    }


    /**
     * 查询所有文字，首页和分类页面需要查询的
     * @param pageNum
     * @param pageSize
     * @param categoryId
     * @return
     */
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId){
//       查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //查询条件
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId)&& categoryId > 0, Article::getCategoryId, categoryId);
        //状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);
//        分页查询 使用分页时需要配置拦截器 com.djt.config.MybatisPlusConfig
        Page<Article> page =new Page<>(pageNum,pageSize);//page.setSize() setCurrent() 每页条数、查询第几页
        page(page,lambdaQueryWrapper);
        List<Article> articles = page.getRecords();

        //        查询categoryName
        // 给article中空的 categoryName 赋值
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .map(article -> article.setViewCount(Long.valueOf(redisCache
                        .getCacheMapValue(SystemConstants.REDIS_VIEW_KEY, article.getId().toString()).toString()
                )))
                .collect(Collectors.toList());

//        封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);
        PageVo pageVo=new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据id查询文章详细内容
     * @param id
     * @return
     */
    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询相应文章内容
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.REDIS_VIEW_KEY, id.toString());
        article.setViewCount(Long.valueOf(viewCount));
        //VO转换
        ArticleDetailVo detailVO = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据 *分类id 查询 *分类名
        Category category = categoryService.getById(detailVO.getCategoryID());
        //不为空时封装响应
        if(category!=null) detailVO.setCategoryName(category.getName());
        return ResponseResult.okResult(detailVO);
    }

    /***
     * 根据文章id增加文章的访问量，注意这里更新的数据存入redis中
     * @param id
     * @return 成功信息
     */
    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中，对应iD的浏览量ViewCount
        redisCache.IncrementCacheMapValue(SystemConstants.REDIS_VIEW_KEY,id.toString(),1);
        return ResponseResult.okResult();

    }

    /**
     * 新增博客
     * @return 200响应码
     */
    @Override
    @Transactional
    public ResponseResult addArticle(ArticleDto articleDto) {
        //将articleDto映射至article并存入表
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客 和 标签 的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    /**
     * 模糊查询数据
     * @param pageNum
     * @param pageSize
     * @param article
     * @return
     */
    @Override
    public PageVo selectArticlePage(Article article, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(article.getTitle()),Article::getTitle, article.getTitle());
        queryWrapper.like(StringUtils.hasText(article.getSummary()),Article::getSummary, article.getSummary());

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,queryWrapper);

        //转换成VO
        List<Article> articles = page.getRecords();

        //这里偷懒没写VO的转换 应该转换完在设置到最后的pageVo中

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(articles);
        return pageVo;
    }

    /**
     * 更新文章时获取文章的信息
     * @param id
     * @return
     */
    @Override
    public ArticleVo getInfo(Long id) {
        Article article = getById(id);
        //获取文章关联tags
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId() );
        List<ArticleTag> articleTags = articleTagService.list(queryWrapper);
        List<Long> tagIdList = articleTags.stream()
                .map(tag -> tag.getTagId())
                .collect(Collectors.toList());
        //Vo转换，将tags封装进属性
        ArticleVo articleVo = BeanCopyUtils.copyBean(article, ArticleVo.class);
        articleVo.setTags(tagIdList);
        return articleVo;
    }

    /**
     * 删除文章，将文章删除表示符标识为1，表示已删除
     * @param id
     */
    @Override
    public void removeById(Long id) {
        LambdaUpdateWrapper<Article> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Article::getId,id);
        updateWrapper.set(Article::getDelFlag,SystemConstants.ARTICLE_STATUS_DELETE);
        update(updateWrapper);
    }

    /**
     * 更新文章
     * @param articleDto
     */
    @Override
    public void edit(ArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        //更新博客信息
        updateById(article);
        //删除原有的 标签和博客的关联
        LambdaQueryWrapper<ArticleTag> articleTagLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleTagLambdaQueryWrapper.eq(ArticleTag::getArticleId,article.getId());
        articleTagService.remove(articleTagLambdaQueryWrapper);
        //添加新的博客和标签的关联信息
        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(articleDto.getId(), tagId))
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTags);

    }

}
