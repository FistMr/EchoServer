package com.puchkov.server;

import java.util.Scanner;

public class ServerRunner {
    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer();
        Thread serverThread = new Thread(echoServer::run);
        serverThread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Напишите 'stop', чтобы остановить сервер");

        while (true) {
            String input = scanner.nextLine();
            if ("stop".equalsIgnoreCase(input)) {
                echoServer.stop();
                System.exit(0);
            }
        }
    }
}
