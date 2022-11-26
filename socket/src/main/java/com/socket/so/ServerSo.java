package com.socket.so;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class ServerSo {
    static long count = 0;

    static ExecutorService pool = new ThreadPoolExecutor(1, 5, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1));

    public static void main(String[] args) {
        ServerSocket serverSocket = getServerSocket();

        while (true) {
            try {
                Socket socket = serverSocket.accept();

                count++;

                System.out.println("当前人数 ==> " + count);

                // runSocketThread(socket);
                runSocketRunnable(socket);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void runSocketThread(Socket socket) {
        new SocketThread(socket).start();
    }

    public static void runSocketRunnable(Socket socket) {
        Runnable s = new SocketRunnable(socket);
        pool.execute(s);
    }

    public static ServerSocket getServerSocket() {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(7777);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return serverSocket;
    }

    public static void run(Socket socket) {
        String remoteSocketAddress = socket.getRemoteSocketAddress().toString();

        try {
            System.out.println("===>");
            System.out.println(remoteSocketAddress + "来了");
            System.out.println("<===");

            InputStream inputStream = socket.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String msg = "";

            while ((msg = bufferedReader.readLine()) != null) {
                System.out.println(remoteSocketAddress + " ==> " + msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("===>");
            System.out.println(remoteSocketAddress + " 溜了!");
            System.out.println("<===");
        }
    }
}
