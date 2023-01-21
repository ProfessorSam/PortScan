package com.github.professorSam.portScan.scanner;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class PortScanTask implements Runnable {
    private final String host;
    private final int start;
    private final int end;
    private final ScanManager manager;

    public PortScanTask(String host, int start, int end, ScanManager manager) {
        this.host = host;
        this.start = start;
        this.end = end;
        this.manager = manager;
    }

    public void run() {
        int pingTimeout = manager.getPing() * 5;
        for (int i = start; i <= end; i++) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress(host, i), pingTimeout);
                System.out.println("Port " + i + " is open on " + socket.getRemoteSocketAddress());
                manager.portCallback(i);
            } catch (IOException e) {
                System.out.println("Port " + i + " is closed!");
            }
        }
    }
}
