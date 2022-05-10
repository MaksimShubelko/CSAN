package com.example.csan.command.impl;

import com.example.csan.command.Command;
import com.example.csan.controller.client.PingClient;

import java.net.SocketException;

public class SetTimeToWaitCommand extends Command {

    @Override
    public void execute(PingClient server) throws SocketException {
        server.setTimeToWait(Integer.parseInt(getData().get("-w")));
    }
}
