package com.puchkov.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientRunner {
    private static final Logger logger = LoggerFactory.getLogger(ClientRunner.class);
    private static final int TIMEOUT_SECONDS = 30;
    private static final int THREAD_POOL_SIZE = 5;
    private static final int CLIENT_COUNT = 5;

    public static void main(String[] args) {
        logger.info("Запуск {} клиентов с пулом из {} потоков", CLIENT_COUNT, THREAD_POOL_SIZE);
        AutomaticEchoClient automaticEchoClient = new AutomaticEchoClient();
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        for (int i = 0; i < CLIENT_COUNT; i++) {
            executorService.execute(automaticEchoClient::run);
        }
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(TIMEOUT_SECONDS, TimeUnit.SECONDS)) {
                logger.warn("Не все задачи завершились в течение {} секунд", TIMEOUT_SECONDS);
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            logger.warn("Главный поток был прерван во время ожидания завершения");
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

    }
}
