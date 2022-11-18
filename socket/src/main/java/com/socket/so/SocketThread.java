package com.socket.so;

import java.net.Socket;

public class SocketThread extends Thread {
    Socket socket;

    public SocketThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ServerSo.run(socket);
    }
}
