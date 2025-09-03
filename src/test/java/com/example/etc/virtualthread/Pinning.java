package com.example.etc.virtualthread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
public class Pinning {

    private final ReentrantLock lock = new ReentrantLock();

    // -Djdk.tracePinnedThreads=full  or -Djdk.tracePinnedThreads=short 를 통해 프로젝트의 코드와 서드파티 라이브러리의 synchronized 선언을 찾을 수 있음
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {

            // synchronized 또는 네이티브 메소드를 실행 중에 IO 블럭이 발생했을 때 버츄얼 스레드가 플랫폼(Java) 스레드에 언마운트 되지 않음
            // 버츄얼 스레드는 IO 블럭이 발생하면 플랫폼 스레드 언마운트하고, 다른 버추얼 스레드가 플랫폼 스레드를 마운트하여 교대로 사용함
            synchronized (this) {
                log.info("1) run. thread: {}", Thread.currentThread());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                log.info("2) run. thread: {}", Thread.currentThread());
            }

            // ReentrantLock을 사용하면, 성능저하 없이 버츄얼 스레드 + synchronized 동작을 대체할 수 있음
            lock.lock();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
    };


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        log.info("1) main. thread: {}", Thread.currentThread());

//        platform();
        virtual();

        log.info("2) main. time: {} , thread: {}", System.currentTimeMillis() - startTime, Thread.currentThread());
    }

    private static void virtual() {
        ThreadFactory factory = Thread.ofVirtual().name("myVirtual-", 0).factory();
        try (ExecutorService executorService = Executors.newThreadPerTaskExecutor(factory)) {
            for (int i = 0; i < 20; i++) {
                Pinning pinning = new Pinning();
                executorService.submit(pinning.runnable);
            }
        }
    }

    private static void platform() {
        try (ExecutorService executorService = Executors.newFixedThreadPool(20)) {
            for (int i = 0; i < 20; i++) {
                Pinning pinning = new Pinning();
                executorService.submit(pinning.runnable);
            }
        }
    }
}
