package com.example.demo.web.v1.virtualthread;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@RestController
@RequiredArgsConstructor
public class VirtualThreadController {

    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/sleep")
    public void sleep() throws InterruptedException {
        // 버츄얼 스레드를 사용했을 때 부하가 없다면 똑같은 플랫폼 스레드를 사용함
        // 하지만, 부하가 클 경우 1)과 2) 로그를 찍는 플랫폼 스레드가 달라질 수 있음
        log.info("1) counter: {}, thread: {}", counter.incrementAndGet(), Thread.currentThread());
        Thread.sleep(5000);
        log.info("2) thread: {}", Thread.currentThread());
    }
}
