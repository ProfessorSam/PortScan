package com.github.professorSam.portScan.scanner;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScanManager {

    private final static int PORT_RANGE = 65535;
    private int THREAD_COUNT;
    private int PORTS_PER_THREAD;
    private final String HOST;
    private final int HOST_PING;
    private List<Integer> openPorts;

    public ScanManager(String host, int threads){
        THREAD_COUNT = threads;
        PORTS_PER_THREAD = PORT_RANGE / THREAD_COUNT;
        this.HOST = host;
        HOST_PING = ping();
    }

    public void scan(){
        openPorts = Collections.synchronizedList(new ArrayList<>());
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            int start = i * PORTS_PER_THREAD + 1;
            int end = start + PORTS_PER_THREAD - 1;
            executor.execute(new PortScanTask(HOST, start, end, this));
        }
        executor.shutdown();
        try {
            if(!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)){
                System.out.println("Port scan timed out!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Scan finished!");
    }

    public void setThreads(int threads){
        THREAD_COUNT = threads;
        PORTS_PER_THREAD = PORT_RANGE / THREAD_COUNT;
    }

    public void printPorts() {
        Collections.sort(openPorts);
        openPorts.forEach(p -> System.out.println("Open: " + p));
    }

    protected void portCallback(int port){
        openPorts.add(port);
    }

    private int ping(){
        long currentTime = System.currentTimeMillis();
        boolean isPinged;
        try {
            isPinged = InetAddress.getByName(HOST).isReachable(2000);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ping failed! Could not find host!");
            return -1;
        }
        currentTime = System.currentTimeMillis() - currentTime;
        if (isPinged) {
            System.out.println("Ping to " + HOST + " is " + currentTime + " millisecond!");
            return (int) currentTime;
        } else {
            System.out.println("Failed to ping " + HOST + "!");
            throw new RuntimeException("Failed ping!");
        }
    }

    protected int getPing(){
        return HOST_PING;
    }
}
