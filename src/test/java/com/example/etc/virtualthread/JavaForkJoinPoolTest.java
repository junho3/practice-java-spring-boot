package com.example.etc.virtualthread;

import java.util.List;
import java.util.Optional;

public class JavaForkJoinPoolTest {

    public static void main(String[] args) {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // 별도의 스레드 풀을 선언하지 않고, parallelStream()으로 병렬처리를 하면 데몬 스레드로 ForkJoinPool을 생성함
//        i: 8 , thread: Thread[#26,ForkJoinPool.commonPool-worker-7,5,main] , daemon: true
//        i: 7 , thread: Thread[#1,main,5,main] , daemon: false
        Optional<Integer> op = list.parallelStream()
            .filter(integer -> {
                System.out.println("i: " + integer + " , thread: " + Thread.currentThread() + " , daemon: " + Thread.currentThread().isDaemon());
                return integer % 2 == 0;
            })
            .findAny();

        System.out.println(op.get());
    }
}
