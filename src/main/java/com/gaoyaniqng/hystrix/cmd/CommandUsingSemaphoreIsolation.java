package com.gaoyaniqng.hystrix.cmd;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyanqing on 2017/8/20.
 */
public class CommandUsingSemaphoreIsolation extends HystrixCommand<String> {

    private final int id;
    private long start,end ;

    public CommandUsingSemaphoreIsolation(int id) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))
                // since we're doing an in-memory cache lookup we choose SEMAPHORE isolation
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                                .withExecutionIsolationSemaphoreMaxConcurrentRequests(3)
                        .withExecutionTimeoutInMilliseconds(30)));
        this.id = id;
    }

    @Override
    protected String run() throws InterruptedException {
        // a real implementation would retrieve data from in memory data structure
        start = System.currentTimeMillis();
        TimeUnit.MILLISECONDS.sleep(id*100);
        return "ValueFromHashMap_" + id;
    }

    @Override
    protected String getFallback(){
        System.out.println(" timecost="+(System.currentTimeMillis() - start));
        return "fallback:"+id;
    }

}