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
 * case3 프로파일
 * 맥스커넥션풀이 25
 * 커넥션 타임아웃 5초
 */
@SpringBootTest
@ActiveProfiles("case3")
public class Case3SpringBootTest {

    @Autowired
    private DemoService demoService;

    /**
     * HikariPool-1 - Connection is not available, request timed out after 5011ms
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
}
