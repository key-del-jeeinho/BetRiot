package com.xylope.betriot.manager;

import com.xylope.betriot.layer.service.command.RootCommand;
import com.xylope.betriot.layer.service.command.custom.HelpCommand;
import com.xylope.betriot.layer.service.command.custom.ProfileCommand;
import com.xylope.betriot.layer.service.command.custom.CreateAccountCommand;
import com.xylope.betriot.layer.service.command.custom.RemoveAccountCommand;
import com.xylope.betriot.layer.service.discord.listener.MessageReceivedListener;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CommandManager implements Manager{
    public static final String ROOT_CMD_STR = "뱃라이엇";
    @Setter
    MessageReceivedListener messageReceivedListener;
    private final RootCommand rootCmd;
    @Setter
    private ProfileCommand profileCommand;
    @Setter
    private CreateAccountCommand createAccountCommand;
    @Setter
    private RemoveAccountCommand removeAccountCommand;
    @Setter
    private HelpCommand helpCommand;

    public CommandManager() {
        this.rootCmd = new RootCommand();
    }

    public void initRootCommand() {
        //leafCommand
        rootCmd.addChildCommand(profileCommand);
        rootCmd.addChildCommand(createAccountCommand);
        rootCmd.addChildCommand(removeAccountCommand);
        rootCmd.addChildCommand(helpCommand);
    }

    @Override
    public void manage() {
        messageReceivedListener.addListener(this::executeCommand);
    }

    public void executeCommand(GuildMessageReceivedEvent event) {
        User user = event.getAuthor();
        String content = event.getMessage().getContentRaw();

        List<String> commandMap = Arrays.asList(content.split(" "));
        if(commandMap.get(0).equals(ROOT_CMD_STR)) {
            String[] args = commandMap.toArray(new String[0]);
            String[] childArgs = new String[args.length - 1];
            System.arraycopy(args, 1, childArgs, 0, childArgs.length);
            rootCmd.execute(event.getChannel(), user, childArgs);
        }
    }
}
