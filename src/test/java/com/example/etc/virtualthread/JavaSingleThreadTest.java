package com.example.etc.virtualthread;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaSingleThreadTest {

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

    public static void main(String[] args) throws InterruptedException {

        // Thread
//        21:21:35.080 [Thread-0] INFO com.example.etc.virtualthread.JavaThreadTest -- 1) run. thread: Thread[#20,Thread-0,5,main]
//        21:21:36.083 [Thread-0] INFO com.example.etc.virtualthread.JavaThreadTest -- 2) run. thread: Thread[#20,Thread-0,5,main]
        Thread thread = new Thread(runnable);
        thread.start();

        // Thread with Demon
        Thread thread1 = new Thread(runnable);
        thread1.setDaemon(true);
        thread1.start();
        thread1.join();

        // Virtual Thread
//        21:26:31.500 [myVirtual] INFO com.example.etc.virtualthread.JavaThreadTest -- 1) run. thread: VirtualThread[#20,myVirtual]/runnable@ForkJoinPool-1-worker-1, class: class java.lang.VirtualThread
//        21:26:32.506 [myVirtual] INFO com.example.etc.virtualthread.JavaThreadTest -- 2) run. thread: VirtualThread[#20,myVirtual]/runnable@ForkJoinPool-1-worker-1
        Thread virtualThread = Thread.ofVirtual().name("myVirtual").start(runnable);
        virtualThread.join(); // ForkJoinPool은 데몬 스레드로 동작함. 데몬 스레드만 있으면 애플리케이션이 종료 됨. 일반 스레드가 있어야 하고, .join()이 있어야 기다려줌
    }
}
