package com.puchkov.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ThreadLocalRandom;

public class AutomaticEchoClient {
    private static final Logger logger = LoggerFactory.getLogger(AutomaticEchoClient.class);
    private static final int SOCKET_TIMEOUT_MS = 5000;
    private static final int CONNECT_TIMEOUT_MS = 3000;


    public void run() {
        try (Socket socket = new Socket()){
            socket.connect(new InetSocketAddress("localhost", 7), CONNECT_TIMEOUT_MS);
            socket.setSoTimeout(SOCKET_TIMEOUT_MS);

            try (DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                 DataInputStream serverInput = new DataInputStream(socket.getInputStream())) {

                logger.info("Поток {} подключился к серверу", Thread.currentThread().getName());

                for (int i = 0; i < 10; i++) {
                    String message = "Сообщение автоматического эхо клиента %s в итерации %d"
                            .formatted(Thread.currentThread().getName().toUpperCase(), i);

                    output.writeUTF(message);
                    String response = serverInput.readUTF();

                    Thread.sleep(ThreadLocalRandom.current().nextLong(500, 1500));//это для симуляции нагрузки

                    logger.info("Ответ сервера: {}", response);
                }
            }
        } catch (SocketTimeoutException e) {
            logger.error("Таймаут операции: {}", e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Поток был прерван");
        } catch (IOException e) {
            logger.error("Ошибка ввода-вывода: {}", e.getMessage());
        }
    }
}

