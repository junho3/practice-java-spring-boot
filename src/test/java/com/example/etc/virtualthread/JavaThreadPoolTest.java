package com.example.etc.virtualthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class JavaThreadPoolTest {

    private static final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            log.info("1) run. thread: {}, class: {}", Thread.currentThread(), Thread.currentThread().getClass());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.info("2) run. thread: {}", Thread.currentThread());
        }
    };

    public static void main(String[] args) {
        log.info("1) main. thread: {}", Thread.currentThread());

        // 일반 스레드 풀
        try (ExecutorService executorService = Executors.newFixedThreadPool(10)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(runnable);
            }
        }

        // 버추얼 스레드 풀
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(runnable);
            }
        }


        log.info("2) main. thread: {}", Thread.currentThread());
    }

    // newFixedThreadPool(1)로 되어 있어서 스레드 풀이지만 버추얼 스레드를 1개만 사용함
    private static void antiPattern1() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newFixedThreadPool(1, factory)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(runnable);
            }
        }
    }

    // 버추얼 스레드로 선언했으나 newSingleThreadExecutor()라서 버추얼 스레드를 1개만 사용함
    private static void antiPattern2() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newSingleThreadExecutor(factory)) {
            for (int i = 0; i < 10; i++) {
                executorService.submit(runnable);
            }
        }
    }
}
