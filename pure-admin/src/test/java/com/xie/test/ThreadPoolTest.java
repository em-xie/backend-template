package com.xie.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.atomic.AtomicInteger;


/**
 * @作者：xie
 * @时间：2023/7/17 13:29
 */
@SpringBootTest
public class ThreadPoolTest {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
//    @Autowired
//    @Qualifier(WS_EXECUTOR)
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor1;


    @Test
    public void  Test1(){
        AtomicInteger i = new AtomicInteger();
        threadPoolTaskExecutor.execute(()->{
            System.out.println(threadPoolTaskExecutor.getThreadNamePrefix()+i);
            i.getAndIncrement();
        });
//        threadPoolTaskExecutor1.execute(()->{
//            System.out.println(threadPoolTaskExecutor1.getThreadNamePrefix()+i);
//        });
    }


}
