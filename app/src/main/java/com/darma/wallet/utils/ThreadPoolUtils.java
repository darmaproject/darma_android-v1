package com.darma.wallet.utils;

import java.util.concurrent.*;

/**
 * Created by Darma Project on 2019/9/30.
 */
public class ThreadPoolUtils  {

    private static ThreadPoolUtils mThreadPoolUtils=new ThreadPoolUtils();
    ThreadPoolExecutor mThreadPoolExecutor;

    private ThreadPoolUtils(){
        mThreadPoolExecutor=new ThreadPoolExecutor(    //
                5, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(10),
                Executors.defaultThreadFactory(),
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        System.out.println(r.toString() + " is discard! ");
                    }
                });
    }

    public static ThreadPoolUtils getInstance(){
        return mThreadPoolUtils;

    }


    public void execute(Runnable runnable){
        mThreadPoolExecutor.execute(runnable);
    }

}
