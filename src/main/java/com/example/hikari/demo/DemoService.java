package com.example.hikari.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DemoService {

    private final BoardRepository boardRepository;

    /**
     * 커넥션을 얻은 후 작업 완료까지 1초가 소요된다.
     */
    @Transactional
    public void addBoard() {
        try {
            // 커넥션 시작
            log.info("case1 start --");
            Thread.sleep(1000);

            Board board = Board.builder()
                    .userId(1L)
                    .title("hello")
                    .build();

            boardRepository.save(board);
            log.info("case1 end --");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 전체 작업은 1초가 걸리지만 커넥션 획득, 반환 시점은 위와 다르다
     */
    public void addBoard2() {
        try {
            log.info("case1 start --");
            Thread.sleep(1000);

            Board board = Board.builder()
                    .userId(1L)
                    .title("hello")
                    .build();

            // 커넥션 시작
            boardRepository.save(board);
            // 커넥션 끝
            log.info("case1 end --");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
