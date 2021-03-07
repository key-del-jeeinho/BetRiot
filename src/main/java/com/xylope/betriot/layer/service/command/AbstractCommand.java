package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCommand implements Executable{
    Map<CommandTrigger, AbstractCommand> childMaps;

    public AbstractCommand() {
        childMaps = new HashMap<>();
    }

    public void addChildCommand(CommandTrigger trigger, AbstractCommand cmd) {
        childMaps.put(trigger, cmd);
    }
}
