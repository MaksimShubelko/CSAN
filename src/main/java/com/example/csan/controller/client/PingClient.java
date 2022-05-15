package com.example.csan.controller.client;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class PingClient implements Runnable {
    private static final int MAX_TIMEOUT = 1000;
    private static int port;
    private final DatagramSocket socket;
    private boolean isInterrupted = false;
    private DatagramPacket ping;
    private String address;
    private final int TTL = 32;
    private int countOfPackets = 10;
    private int length = 32;
    private int timeToWait = 500;
    private boolean isReachability;

    private int sequence_number = 1;

    public void setIP_REGEX(String IP_REGEX) {
        this.IP_REGEX = IP_REGEX;
    }

    private String IP_REGEX;
    private final StringBuilder stringBuffer = new StringBuilder();

    public PingClient() throws SocketException {
        socket = new DatagramSocket(port);
        socket.setSoTimeout(TTL);
    }

    public void run() {
        InetAddress server = null;
        try (socket) {
            if (!Objects.equals(IP_REGEX, "") && !address.matches(IP_REGEX)) {
                stringBuffer.append("Can't resolve address");
            } else {
                server = InetAddress.getByName(address);
                if (isReachability) {
                    stringBuffer.append("Is researchable? ")
                            .append(server.isReachable(timeToWait));
                } else {
                    while (sequence_number - 1 < countOfPackets) {
                        String str = "PING " + sequence_number + " \n";
                        byte[] buf = new byte[length];
                        for (int i = 0; i < length; i++) {
                            if (i < str.getBytes(StandardCharsets.UTF_8).length) {
                                buf[i] = str.getBytes(StandardCharsets.UTF_8)[i];
                            } else {
                                buf[i] = 1;
                            }
                        }
                        ping = new DatagramPacket(buf, buf.length, server, port);
                        long beforeSending;
                        try {
                            beforeSending = System.nanoTime();
                            socket.send(ping);
                        } catch (Exception e) {
                            stringBuffer.append("Timeout for packet ")
                                    .append(sequence_number - 1)
                                    .append("\n");
                            sequence_number++;
                            continue;
                        }
                        DatagramPacket response = new DatagramPacket(new byte[10240], 1024);
                        socket.receive(response);
                        long afterReceiving = System.nanoTime();
                        sequence_number++;
                        if (afterReceiving - beforeSending > timeToWait * 1000L) {
                            stringBuffer.append("Timeout for packet ")
                                    .append(sequence_number - 1)
                                    .append("\n");
                        } else {
                            addData(ping, afterReceiving - beforeSending);
                        }
                    }
                }
            }

        } catch (Exception exception) {
            stringBuffer.append("Incorrect host")
                    .append("\n");
        } finally {
            System.out.println("Client is interrupted");
            isInterrupted = true;
        }
    }

    private void addData(DatagramPacket request, long delayTime) throws Exception {
        byte[] buf = request.getData();
        ByteArrayInputStream bais = new ByteArrayInputStream(buf);
        InputStreamReader isr = new InputStreamReader(bais);
        BufferedReader br = new BufferedReader(isr);
        String line = br.readLine();
        stringBuffer.append("Received from ")
                .append(request.getAddress())
                .append(": ")
                .append(new String(line))
                .append(" TTL ")
                .append(socket.getSoTimeout())
                .append(" buffer ")
                .append(buf.length)
                .append(" count packets ")
                .append(countOfPackets)
                .append(" time ")
                .append(delayTime)
                .append(" nanos \n");
    }

    public StringBuilder getStringBuffer() {
        return stringBuffer;
    }

    public boolean isInterrupted() {
        return isInterrupted;
    }

    public void setCountOfPackets(int countOfPackets) {
        this.countOfPackets = countOfPackets;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setTimeToWait(int timeToWaitMils) throws SocketException {
        timeToWait = timeToWaitMils;
    }

    public void setTimeOut(int timeOutMils) throws SocketException {
        socket.setSoTimeout(timeOutMils);
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setReachability(boolean isReachability) {
        this.isReachability = isReachability;
    }

    public void setIpV4() {
        IP_REGEX = "((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])";
    }

    public void setIpV6() {
        IP_REGEX = "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,7}:|" +
                "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
                "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
                "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
                "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
                "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
                ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
                "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +
                "::(ffff(:0{1,4}){0,1}:){0,1}" +
                "((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}" +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|" +
                "([0-9a-fA-F]{1,4}:){1,4}:" +
                "((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])\\.){3,3}" +
                "(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))";
    }

    public static void setPort(int port) {
        PingClient.port = port;
    }
}
