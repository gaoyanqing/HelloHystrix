package com.gaoyaniqng.hystrix.cmd;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

/**
 * Created by gaoyanqing on 2017/8/20.
 */
public class CommandUsingRequestCache extends HystrixCommand<Boolean> {

    private final int value;

    public CommandUsingRequestCache(int value) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.value = value;
    }

    @Override
    public Boolean run() {
        System.out.println("CommandUsingRequestCache  run value="+value);
        return value == 0 || value % 2 == 0;
    }

    @Override
    public String getCacheKey() {
        //调用三次
        System.out.println("CommandUsingRequestCache  getCacheKey  value="+value);
        return String.valueOf(value);
    }
}
