package com.gaoyaniqng.hystrix;

import com.gaoyaniqng.hystrix.cmd.CommandUsingSemaphoreIsolation;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyanqing on 2017/8/20.
 */
public class SemaphoreTest {
    @Test
    public void maxCurrentRequst() throws InterruptedException {
        int count =4;
        while (count >0){
            int id = count--;
            new Thread(() -> {
                new CommandUsingSemaphoreIsolation(id).execute();
            }).start();
        }
        TimeUnit.SECONDS.sleep(100);
    }

    @Test
    public void timeOutTest(){
        new CommandUsingSemaphoreIsolation(33).execute();
    }
}

