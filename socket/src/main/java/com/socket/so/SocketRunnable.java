package com.socket.so;

import java.net.Socket;

public class SocketRunnable implements Runnable {
    Socket socket;

    public SocketRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        ServerSo.run(socket);
    }

}
