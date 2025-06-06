package com.puchkov.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class AutomaticEchoClient {
    public void run() {
        try (Socket socket = new Socket("localhost", 7);
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverInput = new DataInputStream(socket.getInputStream())) {
            System.out.printf("Поток %s подключился к серверу%n", Thread.currentThread().getName());
            for (int i = 0; i < 10; i++) {
                output.writeUTF("Сообщение автоматического эхо клиента %s в итерации %d".formatted(Thread.currentThread().getName().toUpperCase(), i));
                String response = serverInput.readUTF();
                Thread.sleep(ThreadLocalRandom.current().nextLong(500, 1500)); //это для симуляции нагрузки
                System.out.println("Ответ сервера: " + response);
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}
