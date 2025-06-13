package com.puchkov.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class EchoClient {
    private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 7);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverInput = new DataInputStream(socket.getInputStream())) {

            logger.info("Поток {} подключился к серверу. Введите текст (Ctrl+D для завершения):", Thread.currentThread().getName());

            String message;
            while ((message = consoleInput.readLine()) != null) {
                output.writeUTF(message);
                String response = serverInput.readUTF();
                logger.info("Ответ сервера: {}", response);
            }

        } catch (IOException e) {
            logger.error("Ошибка: {}", e.getMessage());
        }
    }
}