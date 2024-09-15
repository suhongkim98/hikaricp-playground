package com.example.hikari.demo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * case1 프로파일은 맥스커넥션풀이 1
 */
@SpringBootTest
@ActiveProfiles("case1")
public class Case1SpringBootTest {

    @Autowired
    private DemoService demoService;

    /**
     * 맥스 커넥션 풀이 1이므로 커넥션을 기다리는 스레드쪽에서 connection-timeout 시간 후 예외 발생
     * HikariPool-1 - Connection is not available, request timed out after 30032ms
     */
    @Test
    @DisplayName("200개 동시 작업 요청")
    public void test() throws InterruptedException {
        int request = 200;
        CountDownLatch doneSignal = new CountDownLatch(request);
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0 ; i < request ; i++) {
            executor.execute(() -> {
                try {
                    demoService.addBoard();
                    doneSignal.countDown();
                } catch (Exception e) {
                    doneSignal.countDown();
                    e.printStackTrace();
                }
            });
        }
        doneSignal.await();
        executor.shutdown();
    }

    /**
     * 커넥션 획득, 반환 범위가 작아 커넥션 1개로도 충분
     */
    @Test
    @DisplayName("200개 동시 작업 요청 (@Transactional 미사용, 트랜잭션 및 커넥션 획득, 반환 범위 축소)")
    public void test2() throws InterruptedException {
        int request = 200;
        CountDownLatch doneSignal = new CountDownLatch(request);
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0 ; i < request ; i++) {
            executor.execute(() -> {
                demoService.addBoard2();
                doneSignal.countDown();
            });
        }

        doneSignal.await();
        executor.shutdown();
    }
}
