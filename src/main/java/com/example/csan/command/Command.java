package com.example.csan.command;

import com.example.csan.controller.client.PingClient;

import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public abstract class Command {
    private static Map<String, String> data = new HashMap<>();

    public Command() {

    }

    public static void setData(Map<String, String> data) {
        Command.data = data;
    }

    public abstract void execute(PingClient server) throws SocketException;

    public static Map<String, String> getData() {
        return data;
    }
}
