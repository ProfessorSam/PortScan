package com.github.professorSam.portScan;

import com.github.professorSam.portScan.scanner.ScanManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PortScan {

    private static ScanManager currentScanManager;

    public static void main(String[] args){
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("Enter command: ");
            String command;
            try {
                command = reader.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (command.equals("exit")) {
                break;
            }
            handleCommand(command);
        }
    }

    private static void handleCommand(String rawCommand) {
        String baseCommand;
        if(rawCommand.split(" ").length == 0){
            baseCommand = rawCommand;
        } else {
          baseCommand = rawCommand.split(" ")[0];
        }
        switch (baseCommand) {
            case "host" -> handleHost(rawCommand.split(" "));
            case "threads" -> handelThreads(rawCommand.split(" "));
            case "print" -> handlePrint();
            case "scan" -> handleScan();
            case "exit" -> System.exit(0);
            default -> System.out.println("Unknown command: " + baseCommand);
        }
    }

    private static void handleHost(String[] commandArgs) {
        if (commandArgs.length != 2) {
            System.out.println("Please enter a valid command: scan <host>");
            return;
        }
        System.out.println("Configured a standard scan on host " + commandArgs[1] + " with 20 threads");
        currentScanManager = new ScanManager(commandArgs[1], 20);
    }

    private static void handelThreads(String[] commandArgs) {
        if (commandArgs.length != 2) {
            System.out.println("Please enter a valid command: threads <thread count>");
            return;
        }
        int threads;
        try {
            threads = Integer.parseInt(commandArgs[1]);
            if(threads < 1){
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e){
            System.out.println("Please enter a valid number greater than 1!");
            return;
        }
        if(currentScanManager == null){
            System.out.println("Please configure a host first!");
            return;
        }
        currentScanManager.setThreads(threads);
        System.out.println("Set threads to " + threads);
    }

    private static void handlePrint(){
        if(currentScanManager == null) {
            System.out.println("Please configure a host first!");
            return;
        }
        System.out.println("Open Ports: ");
        currentScanManager.printPorts();
    }

    private static void handleScan(){
        if(currentScanManager == null){
            System.out.println("Please configure a host first!");
            return;
        }
        System.out.println("Starting scan!");
        currentScanManager.scan();
    }
}
