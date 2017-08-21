package com.gaoyaniqng.hystrix;

import com.gaoyaniqng.hystrix.cmd.CommandThatFailsFast;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by gaoyanqing on 2017/8/20.
 */
public class FailFastTest {
    @Test
    public void testSuccess() {
        assertEquals("success", new CommandThatFailsFast(false).execute());
    }

    @Test
    public void testFailure() {
        try {
            new CommandThatFailsFast(true).execute();
            fail("we should have thrown an exception");
        } catch (HystrixRuntimeException e) {
            assertEquals("failure from CommandThatFailsFast", e.getCause().getMessage());
            e.printStackTrace();
        }
    }
}
