package com.puchkov.client;

import java.io.*;
import java.net.Socket;

public class EchoClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 7);
             BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));
             DataOutputStream output = new DataOutputStream(socket.getOutputStream());
             DataInputStream serverInput = new DataInputStream(socket.getInputStream())) {

            System.out.printf("Поток %s подключился к серверу. Введите текст (Ctrl+D для завершения):%n", Thread.currentThread().getName());

            String message;
            while ((message = consoleInput.readLine()) != null) {
                output.writeUTF(message);
                String response = serverInput.readUTF();
                System.out.println("Ответ сервера: " + response);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}