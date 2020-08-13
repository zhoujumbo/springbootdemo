package com.base;

import com.jum.SpringbootdemoApplication;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SpringbootdemoApplication.class)
public class ServiceTest {


    @BeforeClass
    public static void init() {
        System.setProperty("spring.profiles.active", "dev");
        System.setProperty("DEMO-HOME-CONFIG", "D:\\temp");
    }


    /**
     *
     */
    @Test
    public void getData() throws Exception {
//        System.out.println(sysManagers.toString());
    }


}
