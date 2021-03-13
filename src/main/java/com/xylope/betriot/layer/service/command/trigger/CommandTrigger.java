package com.xylope.betriot.layer.service.command.trigger;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
public class CommandTrigger implements Trigger<String> {
    private final String command;

    @Override
    public boolean checkTrigger(String arg) {
        return arg.equals(command);
    }

    //to use for map's key
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommandTrigger trigger = (CommandTrigger) o;
        return Objects.equals(command, trigger.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command);
    }

    @Override
    public String toString() {
        return command;
    }
}
