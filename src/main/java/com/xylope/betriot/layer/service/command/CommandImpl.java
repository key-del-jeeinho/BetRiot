package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

public class CommandImpl extends AbstractCommand{
    @Override
    public void execute(GuildChannel channel, User sender, String... args) {
        if(sender.isBot()) return; //bot can't use command

        if(args.length > 0) {
            children.forEach((child) -> {
                        CommandTrigger trigger = child.trigger;
                        if (trigger.checkTrigger(args[0])) {
                            String[] childArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, childArgs, 0, childArgs.length);
                            child.execute(channel, sender, childArgs);
                        }
                    }
            );
        }
    }
}
