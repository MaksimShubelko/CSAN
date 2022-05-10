package com.example.csan.command.impl;

import com.example.csan.command.Command;
import com.example.csan.controller.client.PingClient;

import java.net.SocketException;

public class SetTimeOutCommand extends Command {
    @Override
    public void execute(PingClient server) throws SocketException {
        server.setTimeOut(Integer.parseInt(getData().get("-i")));
    }
}
