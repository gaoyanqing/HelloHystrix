package com.gaoyaniqng.hystrix.cmd;

import com.netflix.hystrix.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by gaoyanqing on 2017/8/19.
 */
public class CommandHelloFailure extends HystrixCommand<String> {

    private final String name;

    public CommandHelloFailure(String name) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))  //必须
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(1000))  //超时时间
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("ExampleGroup-pool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3)));

        this.name = name;
    }

    public CommandHelloFailure(String msg,String cmdKey,String poolkey) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"))  //必须
                .andCommandKey(HystrixCommandKey.Factory.asKey(cmdKey))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(1000))  //超时时间
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(poolkey))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(3)));

        this.name = msg;
    }

    @Override
    protected String run() throws InterruptedException {
        String theadName = this.getThreadPoolKey().name();
        String cmdKey=this.getThreadPoolKey().name();
        System.out.println(" run begin , threadPool="+theadName+" cmdKey="+cmdKey);
        if("Exception".equals(name)) {
            throw new RuntimeException("this command always fails");
        }else if("Timeout".equals(name)){
            TimeUnit.SECONDS.sleep(2);
        }else if("Reject".equals(name)){
            TimeUnit.MILLISECONDS.sleep(800);
        }
        System.out.println(" run end");

        return "Hello " + name + "!";
    }

    @Override
    protected String getFallback() {
        StringBuilder sb = new StringBuilder(" run fallback");
        boolean isRejected = isResponseRejected();
        boolean isException = isFailedExecution();
        boolean isTimeout= isResponseTimedOut();
        boolean isCircut = isCircuitBreakerOpen();

        sb.append(" ,isRejected:").append(isRejected);
        sb.append(" ,isException:"+isException);
        if(isException){
            sb.append(" msg=").append(getExecutionException().getMessage());
        }
        sb.append(",isTimeout: "+isTimeout);
        sb.append(", isCircut:"+isCircut);

        System.out.println(sb.toString());

        String msg="Hello Failure " + name + "!";
        return msg;
    }
}
