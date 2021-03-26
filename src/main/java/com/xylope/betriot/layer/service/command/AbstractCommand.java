package com.xylope.betriot.layer.service.command;

import com.xylope.betriot.layer.service.command.trigger.CommandTrigger;
import lombok.Setter;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.User;

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

    public void checkChildExecute(GuildChannel channel, User sender, String... args) {
        if(args.length > 0) {
            children.forEach((child) -> {
                        if (child.trigger.checkTrigger(args[0])) {
                            String[] childArgs = new String[args.length - 1];
                            System.arraycopy(args, 1, childArgs, 0, childArgs.length);
                            child.execute(channel, sender, childArgs);
                        }
                    }
            );
        }
    }
}
