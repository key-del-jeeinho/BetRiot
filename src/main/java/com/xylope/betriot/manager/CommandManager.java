package com.xylope.betriot.manager;

import com.xylope.betriot.layer.service.command.RootCommand;
import com.xylope.betriot.layer.service.command.custom.*;
import com.xylope.betriot.layer.service.command.custom.bet.BetCommand;
import com.xylope.betriot.layer.service.command.custom.bet.BetOpenCommand;
import com.xylope.betriot.layer.service.command.custom.bet.BettingParticipationCommand;
import com.xylope.betriot.layer.service.command.custom.notice.NoticeAllCommand;
import com.xylope.betriot.layer.service.command.custom.notice.NoticeCheckCommand;
import com.xylope.betriot.layer.logic.discord.listener.MessageReceivedListener;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class
CommandManager implements Manager{
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
    @Setter
    private NoticeCheckCommand noticeCommand;
    @Setter
    private NoticeAllCommand noticeAllCommand;
    @Setter
    private KillCodeCommand killCodeCommand;
    @Setter
    private MatchCommand matchCommand;
    @Setter
    private BetCommand betCommand;
    @Setter
    private BetOpenCommand betOpenCommand;
    @Setter
    private BettingParticipationCommand bettingParticipationCommand;
    @Setter
    private CheckPointCommand checkPointCommand;

    public CommandManager() {
        this.rootCmd = new RootCommand();
    }

    public void initRootCommand() {
        rootCmd.addChildCommand(noticeCommand);
        noticeCommand.addChildCommand(noticeAllCommand);

        rootCmd.addChildCommand(betCommand);
        betCommand.addChildCommand(betOpenCommand);
        betCommand.addChildCommand(bettingParticipationCommand);
        //leafCommand
        rootCmd.addChildCommand(profileCommand);
        rootCmd.addChildCommand(createAccountCommand);
        rootCmd.addChildCommand(removeAccountCommand);
        rootCmd.addChildCommand(helpCommand);
        rootCmd.addChildCommand(killCodeCommand); //TODO 임시 커맨드 릴리즈 후 무조건 삭제
        rootCmd.addChildCommand(matchCommand);
        rootCmd.addChildCommand(checkPointCommand);
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

    public static String getRawToArgs(String... args) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < args.length; i++) {
            result.append(args[i]);
            if (i < args.length - 1) result.append(" ");
        }
        return result.toString();
    }
}
