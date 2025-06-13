package com.puchkov.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class ServerRunner {
    private static final Logger logger = LoggerFactory.getLogger(ServerRunner.class);
    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer();
        Thread serverThread = new Thread(echoServer::run);
        serverThread.start();

        Scanner scanner = new Scanner(System.in);
        logger.info("Напишите 'stop', чтобы остановить сервер");
        while (true) {
            String input = scanner.nextLine();
            if ("stop".equalsIgnoreCase(input)) {
                echoServer.stop();
                System.exit(0);
            }
        }
    }
}
