package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCommand implements Executable{
    @Setter
    protected CommandTrigger trigger;
    protected List<AbstractCommand> children;

    public AbstractCommand() {
        children = new ArrayList<>();
    }

    public void addChildCommand(AbstractCommand cmd) {
        children.add(cmd);
    }
}
