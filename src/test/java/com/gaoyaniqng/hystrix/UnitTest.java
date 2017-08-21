package com.gaoyaniqng.hystrix;

import com.gaoyaniqng.hystrix.cmd.CommandHelloWorld;
import com.gaoyaniqng.hystrix.cmd.CommandObservableHelloWorld;
import org.junit.Test;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import static java.lang.System.out;

import static org.junit.Assert.assertEquals;

/**
 * Created by gaoyanqing on 2017/8/19.
 */
public  class UnitTest {
    @Test
    public void testSynchronous() throws ExecutionException, InterruptedException {
        assertEquals("Hello World!", new CommandHelloWorld("World").execute());
        assertEquals("Hello Bob!", new CommandHelloWorld("Bob").execute());

    }

    @Test
    public void testAsynchronous1() throws Exception {
        assertEquals("Hello World!", new CommandHelloWorld("World").queue().get());
        assertEquals("Hello Bob!", new CommandHelloWorld("Bob").queue().get());
    }

    @Test
    public void testAsynchronous2() throws Exception {

        Future<String> fWorld = new CommandHelloWorld("World").queue();
        Future<String> fBob = new CommandHelloWorld("Bob").queue();

        assertEquals("Hello World!", fWorld.get());
        assertEquals("Hello Bob!", fBob.get());
    }

    @Test
    public void testObservable() throws Exception {

        Observable<String> fWorld = new CommandHelloWorld("World").observe();
        Observable<String> fBob = new CommandHelloWorld("Bob").observe();

        // blocking
        assertEquals("Hello World!", fWorld.toBlocking().single());
        assertEquals("Hello World!", fWorld.toBlocking().toFuture().get());

        assertEquals("Hello Bob!", fBob.toBlocking().single());

        // non-blocking
        // - this is a verbose anonymous inner-class approach and doesn't do assertions
        fWorld.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                out.println(" onCompleted ");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                System.out.println("onNext: " + v);
            }
        });

        // non-blocking
        // - also verbose anonymous inner-class
        // - ignore errors and onCompleted signal
        fBob.subscribe(new Action1<String>() {

            @Override
            public void call(String v) {
                System.out.println("onNext: " + v);
            }

        });
    }



}
