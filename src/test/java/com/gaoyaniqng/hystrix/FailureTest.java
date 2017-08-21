package com.gaoyaniqng.hystrix;

import com.gaoyaniqng.hystrix.cmd.CommandHelloFailure;
import com.gaoyaniqng.hystrix.cmd.CommandHelloWorld;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * Created by gaoyanqing on 2017/8/19.
 */
public class FailureTest {
    @Test
    public void expTest() {
        assertEquals("Hello Exception!", new CommandHelloFailure("Exception").execute());
    }

    @Test
    public void timeOutTest() {
        long start = System.currentTimeMillis();
        new CommandHelloFailure("Timeout").execute();
        System.out.println(System.currentTimeMillis() - start);

        //assertEquals("Hello Timeout!", new CommandHelloFailure("Timeout").execute());
    }

    @Test
    public void rejectTest() throws InterruptedException {
        int count = 100;
        while (count-- > 0){
            new CommandHelloFailure("Reject").queue();
            TimeUnit.MILLISECONDS.sleep(100);
            System.out.println(count);
        }
    }
}
