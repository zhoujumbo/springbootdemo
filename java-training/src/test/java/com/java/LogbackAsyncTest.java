package com.java;/**
 * @Description： TODO
 * @Author: zhoujumbo
 * @Date: ${Time} ${Date}
 */

import com.fortunetree.basic.support.commons.business.logger.LogUtil;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @ClassName LogbackAsyncTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/9/19
 * @Version 1.0
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = BackApplication.class)
public class LogbackAsyncTest {


    @BeforeClass
    public static void init() {
        System.setProperty("logging.level.root", "info");
        System.setProperty("logging.level.org.springframework.web", "dev");
        System.setProperty("spring.profiles.active", "dev");
        System.setProperty("BLINDBOX-HOME-CONFIG", "D:\\temp\\swoa2_config\\blindbox\\");
    }


    @Test
    public void test01() {

        for (int i = 0; i < 10000; i++) {
            LogUtil.info("######is  INFO######################");
            LogUtil.warn("######is  WARN######################");
            LogUtil.error("#######is  ERROR########################");

            if (i % 1000 == 0) {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        }
    }

    @Test
    public void test02() {

        LogUtil.info("######is  INFO######################");
        LogUtil.warn("######is  WARN######################");
        LogUtil.error("#######is  ERROR########################");

    }


}
