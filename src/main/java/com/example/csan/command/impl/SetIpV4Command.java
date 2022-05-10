package com.example.csan.command.impl;

import com.example.csan.command.Command;
import com.example.csan.controller.client.PingClient;

import java.net.SocketException;

public class SetIpV4Command extends Command {
    @Override
    public void execute(PingClient server) throws SocketException {
        server.setIpV4();
    }
}
