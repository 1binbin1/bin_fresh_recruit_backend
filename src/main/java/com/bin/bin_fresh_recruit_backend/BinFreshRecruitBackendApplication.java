package com.bin.bin_fresh_recruit_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author hongxiaobin
 */
@SpringBootApplication()
@MapperScan("com.bin.bin_fresh_recruit_backend.mapper")
@EnableScheduling
public class BinFreshRecruitBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BinFreshRecruitBackendApplication.class, args);
    }

}
