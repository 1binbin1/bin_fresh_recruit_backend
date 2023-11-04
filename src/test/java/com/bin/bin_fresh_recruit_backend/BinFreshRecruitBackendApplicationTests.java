package com.bin.bin_fresh_recruit_backend;

import com.bin.bin_fresh_recruit_backend.utils.IdUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BinFreshRecruitBackendApplicationTests {

    @Test
    void contextLoads() {
        String id1 = IdUtils.getId(0);
        String id2 = IdUtils.getId(1);
        String id3 = IdUtils.getId(2);
        System.out.println(id1);
        System.out.println(id2);
        System.out.println(id3);
    }

}
