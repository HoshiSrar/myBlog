package com.djt.domain.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2023-03-10 20:29:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article_tag")
public class ArticleTag  {
    //文章id
    @TableId
    private Long articleId;
    //标签id
    //@TableId
    private Long tagId;




}

