package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.LongStream;

@RestController
@RequestMapping("/stream")
public class StreamController {

    private static final long SUM_LIMIT = 1_000_000L;
    private static final Logger logger = LoggerFactory.getLogger(StreamController.class);

    @GetMapping("/sum")
    public Long getSum() {
        logger.info("Was invoked method for calculate sum with parallel stream");

        long startTime = System.currentTimeMillis();

        long result = LongStream.rangeClosed(1, SUM_LIMIT)
                .parallel()  // Ключевое ускорение - параллельная обработка
                .sum();

        long endTime = System.currentTimeMillis();
        logger.info("Parallel stream calculation took {} ms", (endTime - startTime));

        return result;
    }

    // Самый быстрый способ
    @GetMapping("/sum-formula")
    public Long getSumFormula() {
        logger.info("Was invoked method for calculate sum with arithmetic formula");

        long startTime = System.currentTimeMillis();
        // Формула суммы арифметической прогрессии: S = n * (n + 1) / 2
        long sum = SUM_LIMIT * (SUM_LIMIT + 1) / 2;
        long endTime = System.currentTimeMillis();

        logger.info("Formula calculation took {} ms", (endTime - startTime));
        return sum;
    }
}
