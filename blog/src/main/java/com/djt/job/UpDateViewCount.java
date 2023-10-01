package com.djt.job;

import com.djt.annotation.SystemLog;
import com.djt.constants.SystemConstants;
import com.djt.domain.entity.Article;
import com.djt.service.ArticleService;
import com.djt.utils.RedisCache;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UpDateViewCount {
    @Resource
    private RedisCache redisCache;
    @Resource
    private ArticleService articleService;
    @Scheduled(cron = "0 0/10 * * * ?")
    private void upDateViewCount(){
        //每隔10分钟将redis中的viewCount数据记录到mybatis中
        //1.读取Redis中的ViewCount数据
        try {
            Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.REDIS_VIEW_KEY);

            List<Article> articles = viewCountMap.entrySet().stream()
                    .map(entry -> new Article(Long.valueOf(entry.getKey()), Long.valueOf(entry.getValue())))
                    .collect(Collectors.toList());

            //2.存入myBatis中
            articleService.updateBatchById(articles);
        }catch (Exception e){
            e.printStackTrace();
            log.info("存储Redis发生错误：{}",e.getMessage());
        }


    }

}
