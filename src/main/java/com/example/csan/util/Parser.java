package com.example.csan.util;

import com.example.csan.command.CommandType;

import java.util.HashMap;
import java.util.Map;

public class Parser {
    private Map<String, String> commandsMap = new HashMap<>();
    ;
    private String address;
    private String IPV4_REGEX = "";

    public String getAddress() {
        return address;
    }

    public Map<String, String> parseQuery(String query) {
        String[] commands = query.split(" ");
        address = commands[commands.length - 1];
        int i = 0;
        while (i < commands.length) {
            if (commands[i].matches(CommandType.IS_REACHABLE.getCommand())) {
                commandsMap.put(commands[i], null);
                i++;
            } else {
                if (commands[i].matches(CommandType.RES_ADDRESS.getCommand())) {
                    commandsMap.put(commands[i], null);
                    i++;
                } else {
                    if (commands[i].matches(CommandType.SET_TIME_TO_WAIT.getCommand())) {
                        if (commands[i + 1].matches(CommandType.SET_TIME_TO_WAIT.getForm())) {
                            commandsMap.put(commands[i], commands[i + 1]);
                        }
                        i++;
                    } else {
                        if (commands[i].matches(CommandType.SET_COUNT.getCommand())) {
                            if (commands[i + 1].matches(CommandType.SET_COUNT.getForm())) {
                                commandsMap.put(commands[i], commands[i + 1]);
                            }
                            i++;
                        } else {
                            if (commands[i].matches(CommandType.SET_LENGTH.getCommand())) {
                                System.out.println(commands[i + 1].matches(CommandType.SET_LENGTH.getForm()));
                                if (commands[i + 1].matches(CommandType.SET_LENGTH.getForm())) {
                                    commandsMap.put(commands[i], commands[i + 1]);
                                }
                            } else {
                                if (commands[i].matches(CommandType.SET_IPV4.getCommand())) {
                                    commandsMap.put(commands[i], null);
                                } else {
                                    if (commands[i].matches(CommandType.SET_IPV6.getCommand())) {
                                        commandsMap.put(commands[i], null);
                                    }
                                }
                            }
                            i++;
                        }
                    }
                }
            }

        }

        return commandsMap;
    }
}
