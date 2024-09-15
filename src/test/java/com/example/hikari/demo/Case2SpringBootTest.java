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
 * case2 프로파일은 맥스커넥션풀이 25
 */
@SpringBootTest
@ActiveProfiles("case2")
public class Case2SpringBootTest {

    @Autowired
    private DemoService demoService;

    /**
     * 커넥션 관련 예외 없이 성공
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
