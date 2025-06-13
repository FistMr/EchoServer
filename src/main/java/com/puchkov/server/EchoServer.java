package com.puchkov.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private static final Logger logger = LoggerFactory.getLogger(EchoServer.class);
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
            logger.info("Сервер запущен на порту {}", port);
            while (isRunning) {
                var socket = serverSocket.accept();
                executorService.execute(() -> processSocket(socket));
            }
        } catch (IOException e) {
            if (!isRunning) {
                logger.error("Сервер остановлен");
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
            logger.error("Сервер уже остановлен");
        }
        executorService.shutdown();
    }

    private void processSocket(Socket socket) {
        try (socket;
             var inputStream = new DataInputStream(socket.getInputStream());
             var outputStream = new DataOutputStream(socket.getOutputStream())) {
            logger.info("клиент подключился");
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            logger.info("клиент отключился");
        } catch (IOException e) {
            if (!isRunning) {
                logger.error("Обработка соединения прервана из-за остановки сервера");
            } else {
                logger.error("Ошибка в обработке соединения или клиент принудительно отключился: {}", e.getMessage());
            }
        }
    }
}
