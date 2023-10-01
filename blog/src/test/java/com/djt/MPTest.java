package com.djt;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.djt.domain.entity.Link;
import com.djt.mapper.LinkMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MPTest {
    @Resource
    LinkMapper mapper;
    @Test
    public void testLambdaWrapper(){
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Link::getName);
        //queryWrapper.select("id","title",)
        System.out.println(mapper.selectList(queryWrapper));
        //System.out.println(mapper.);
    }
    @Test
    public void MM(){
        System.out.println("123");
    }


}
