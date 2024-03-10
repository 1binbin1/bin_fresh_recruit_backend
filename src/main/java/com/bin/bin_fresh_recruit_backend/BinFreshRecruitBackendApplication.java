package com.bin.bin_fresh_recruit_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author hongxiaobin
 */
@SpringBootApplication()
@MapperScan("com.bin.bin_fresh_recruit_backend.mapper")
@EnableScheduling
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 6048000)
public class BinFreshRecruitBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinFreshRecruitBackendApplication.class, args);
    }

}
