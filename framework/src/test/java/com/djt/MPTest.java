package com.djt;

import com.djt.mapper.UserMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class MPTest {

    @Resource
    private UserMapper userMapper;


    @Test
    public void testQueryList(){
        //System.out.println(userMapper.selectList(null));
        //System.out.println(userMapper.selectById(1));
    }

}
