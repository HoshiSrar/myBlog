package com.djt;

import com.djt.mapper.LinkMapper;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest()
public class proxy {
    @Resource
    LinkMapper linkMapper;
    @Test
    public void test(){
        String str = "LoginUser(user=User(id=3, userName=22, nickName=咸鱼也有梦想, password=$2a$10$Jnq31rRkNV3RNzXe0REsEOSKaYK8UgVZZqlNlNXqn.JeVcj2NdeZy, type=1, status=0, email=null, phonenumber=15042968936, sex=0, avatar=http://rr4li3mz7.bkt.clouddn.com/2023/03/07/24777a2cdf484a53a2fb87bcde71d403.png, createBy=null, createTime=Thu Jan 05 21:28:43 CST 2023, updateBy=null, updateTime=Mon May 15 23:41:15 CST 2023, delFlag=0, roleIds=null), permission=[])";
        str = str.split("nickName=",3)[1];
        String[] nickNames = str.split(", pass",3);
        for (String name : nickNames) {
            System.out.println(name);
        }
    }
}
