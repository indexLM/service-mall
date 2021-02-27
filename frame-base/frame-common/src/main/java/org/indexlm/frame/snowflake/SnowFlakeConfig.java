package org.indexlm.frame.snowflake;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 雪花算法
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Component
public class SnowFlakeConfig {

    @Bean(name = "snowflake")
    public Snowflake getSnowflake(){
        return IdUtil.getSnowflake(RandomUtil.randomInt(0,31), RandomUtil.randomInt(0,31));
    }
}
