package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;

public class RootCommand extends AbstractCommand{
    @Override
    public void execute(String... args) {
        for(CommandTrigger trigger : childMaps.keySet()) {
            if(trigger.checkTrigger(args[0])) {
                String[] childArgs = new String[args.length-1];
                System.arraycopy(args, 1, childArgs, 0, childArgs.length);
                childMaps.get(trigger).execute(childArgs);
            }
        }
    }
}
