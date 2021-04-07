package com.xylope.betriot.layer.service.command.custom.bet;

import com.xylope.betriot.layer.service.bet.BetService;
import com.xylope.betriot.layer.service.bet.handler.DiscordBetReaderExceptionHandler;
import com.xylope.betriot.layer.service.bet.reader.Action;
import com.xylope.betriot.layer.service.bet.reader.BetReader;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BettingParticipationCommand extends LeafCommand {
    public BettingParticipationCommand(BetService service, BetReader<String[]> reader, DiscordBetReaderExceptionHandler handler) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                if(args.length < 3) {
                    //TODO 충분한 인자값이 전달되지 않았을경우, Usage 를 유저에게 보내도록 로직 수정
                    return;
                }
                TextChannel textChannel = channel.getGuild().getTextChannelById(channel.getId());
                handler.setTextChannel(textChannel);
                handler.handling(reader, service, Action.BETTING_PARTICIPATION,
                        sender.getId(), args[0], args[1], args[2]);
            }
        });
    }
}

