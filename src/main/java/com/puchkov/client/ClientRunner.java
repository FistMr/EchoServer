package com.puchkov.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientRunner {
    public static void main(String[] args) {
        AutomaticEchoClient automaticEchoClient = new AutomaticEchoClient();
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            executorService.execute(automaticEchoClient::run);
        }
        executorService.shutdown();
    }
}
