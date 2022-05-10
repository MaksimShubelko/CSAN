package com.example.csan.command;

import com.example.csan.command.impl.*;

public enum CommandType {
    SET_LENGTH("-l", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$", new SetLengthCommand()),
    SET_TIME_TO_WAIT("-w", "^[1-9][0-9]{0,2}|1000$", new SetTimeToWaitCommand()),
    SET_IPV4("-4", "", new SetIpV4Command()),
    SET_IPV6("-6", "", new SetIpV6Command()),
    SET_COUNT("-n", "^([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])$", new SetCountOfPacketsCommand()),
    RES_ADDRESS("-a", "", new SetResolvingAddressCommand()),
    SET_TIME_OUT("-i", "^[1-9][0-9]{0,2}|1000$", new SetTimeOutCommand()),
    IS_REACHABLE("-p", "", new IsReachableCommand());

    String command;
    String form;
    Command commandToExecute;

    CommandType(String command, String form, Command commandToExecute) {
        this.command = command;
        this.form = form;
        this.commandToExecute = commandToExecute;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public Command getCommandToExecute() {
        return commandToExecute;
    }

    public void setCommandToExecute(Command commandToExecute) {
        this.commandToExecute = commandToExecute;
    }
}
