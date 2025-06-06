package com.puchkov.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private final static int port = 7;
    private final ExecutorService executorService;
    private volatile boolean isRunning = true;
    private ServerSocket serverSocket;

    public EchoServer() {
        executorService = Executors.newCachedThreadPool();
    }

    public void run() {
        try (var serverSocket = new ServerSocket(port)) {
            this.serverSocket = serverSocket;
            System.out.printf("Сервер запущен на порту %S %n", port);
            while (isRunning) {
                var socket = serverSocket.accept();
                executorService.execute(() -> processSocket(socket));
            }
        } catch (IOException e) {
            if (!isRunning) {
                System.out.println("Сервер остановлен");
                return;
            }
            throw new RuntimeException(e);
        }


    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Сервер уже остановлен");
        }
    }

    private void processSocket(Socket socket) {
        try (socket;
             var inputStream = new DataInputStream(socket.getInputStream());
             var outputStream = new DataOutputStream(socket.getOutputStream())) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
