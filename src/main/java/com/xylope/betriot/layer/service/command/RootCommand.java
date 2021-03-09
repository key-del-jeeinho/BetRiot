package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;
import net.dv8tion.jda.api.entities.User;

public class RootCommand extends AbstractCommand{
    @Override
    public void execute(User sender, String... args) {
        for(CommandTrigger trigger : childMaps.keySet()) {
            if(trigger.checkTrigger(args[0])) {
                String[] childArgs = new String[args.length-1];
                System.arraycopy(args, 1, childArgs, 0, childArgs.length);
                childMaps.get(trigger).execute(sender, childArgs);
            }
        }
    }
}
