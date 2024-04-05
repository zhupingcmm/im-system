package com.ocbc.im.common.enums.command;

public enum MessageCommand implements Command{

    /**
     *  JSON 4
     */
    JSON(0x0);
    private final int command;

    MessageCommand(int command) {
        this.command = command;
    }

    @Override
    public int getCommand() {
        return this.command;
    }

}
