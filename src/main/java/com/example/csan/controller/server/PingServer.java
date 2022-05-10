package com.example.csan.controller.server;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;

public class PingServer implements Runnable {
    private static final double LOSS_RATE = 0.3;
    private static final int AVERAGE_DELAY = 100;
    private final int port = 9991;
    private final DatagramSocket socket;
    private boolean isInterrupted = false;
    private int bufferSize = 32;


    public PingServer() throws SocketException {
        socket = new DatagramSocket(port);
    }

    public void run() {

        Random random = new Random();
        try(socket) {
            while (!Thread.currentThread().isInterrupted()) {
                DatagramPacket request = new DatagramPacket(new byte[bufferSize], bufferSize);
                socket.receive(request);
                printData(request);
                socket.close();
                InetAddress clientHost = request.getAddress();
                int clientPort = request.getPort();
                byte[] buf = request.getData();
                DatagramPacket reply = new DatagramPacket(buf, buf.length, clientHost, clientPort);
                socket.send(reply);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Server is interrupted");
            isInterrupted = true;
        }
    }

    private void printData(DatagramPacket request) throws Exception {
        byte[] buf = request.getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        InputStreamReader isr = new InputStreamReader(bais);
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();
        System.out.println(
                "Received from " +
                        request.getAddress().getHostAddress() +
                        ": " +
                        new String(line) + "TTL" + socket.getSoTimeout() + " buffer " + bufferSize);
    }

}