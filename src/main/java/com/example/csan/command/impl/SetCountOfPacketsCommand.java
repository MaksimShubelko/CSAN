package com.example.csan.command.impl;

import com.example.csan.command.Command;
import com.example.csan.controller.client.PingClient;

import java.util.Map;

public class SetCountOfPacketsCommand extends Command {

    @Override
    public void execute(PingClient server) {
        server.setCountOfPackets(Integer.parseInt(getData().get("-n")));
    }
}
