package com.socket.so;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Objects;
import java.util.Scanner;

import org.springframework.stereotype.Component;

@Component
public class ClientSo {
    static Socket socket;

    public static void main(String[] args) {
        getSocket();

        if (socket != null) {
            try {
                OutputStream outputStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);

                Scanner scanner = new Scanner(System.in);

                while (scanner.hasNextLine()) {
                    String msg = scanner.nextLine();

                    if (Objects.equals(msg, "")) {
                        scanner.close();
                        return;
                    }

                    printStream.println(msg);
                }

                printStream.println("Over");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Socket getSocket() {
        try {
            socket = new Socket("127.0.0.1", 7777);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return socket;
    }
}
