package com.example.csan.command.impl;

import com.example.csan.command.Command;
import com.example.csan.command.CommandType;

import java.util.Arrays;

public class CommandProvider {
    private static Command command;

    public static Command defineCommand(String commandName) {
            CommandType[] type = CommandType.values();
            for (CommandType commandType : type) {
                if (commandType.getCommand().equals(commandName)) {
                    command = commandType.getCommandToExecute();
                }
            }
        return command;
    }
}
