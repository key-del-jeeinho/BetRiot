package com.xylope.betriot.layer.service.command.custom.bet;

import com.xylope.betriot.exception.NotEnoughMoneyException;
import com.xylope.betriot.layer.domain.dao.UserDao;
import com.xylope.betriot.layer.domain.vo.UserVO;
import com.xylope.betriot.layer.service.bet.BetService;
import com.xylope.betriot.layer.service.bet.UnknownBetIdException;
import com.xylope.betriot.layer.service.command.AbstractCommand;
import com.xylope.betriot.layer.service.command.LeafCommand;
import com.xylope.betriot.layer.service.message.ChannelErrorMessageSender;
import com.xylope.betriot.layer.service.message.ChannelMessageSenderImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class BettingParticipationCommand extends LeafCommand {
    public BettingParticipationCommand(ChannelErrorMessageSender channelErrorMessageSender, ChannelMessageSenderImpl channelMessageSender, BetService betService, UserDao dao) {
        super(new AbstractCommand() {
            @Override
            public void execute(GuildChannel channel, User sender, String... args) {
                Guild guild = channel.getGuild();
                TextChannel textChannel = guild.getTextChannelById(channel.getId());
                assert textChannel != null;

                int betId;
                boolean isBetWin = false;
                int betMoney;

                try {
                    betId = Integer.parseInt(args[0]); //배팅에 걸 돈을 가져온다
                } catch (NumberFormatException e) {
                    channelMessageSender.sendMessage(textChannel, "잘못된 인자입니다! \n 배팅아이디는 항상 정수값 이어야 합니다.");
                    return;
                }

                String isBetWinOrLose = args[1];
                if(isBetWinOrLose.equals(DisplayWinOrLose.WIN.getDisplay()))
                    isBetWin = true;
                else if(!isBetWinOrLose.equals(DisplayWinOrLose.LOSE.getDisplay())) {//Win Lose 둘 다 아닐경우
                    channelMessageSender.sendMessage(textChannel, "잘못된 인자입니다! \n 승리 혹은 패배 둘중 하나만 입력해주세요!");
                    return;
                }

                try {
                    betMoney = Integer.parseInt(args[2]); //배팅에 걸 돈을 가져온다
                } catch (NumberFormatException e) {
                    channelMessageSender.sendMessage(textChannel, "잘못된 인자입니다! \n 배팅하실 돈은 항상 정수값 이어야 합니다.");
                    return;
                }

                try {
                    UserVO user = dao.get(sender.getIdLong());
                    int possessionMoney = user.getMoney();
                    if(possessionMoney < betMoney + 1000)
                        throw new NotEnoughMoneyException(possessionMoney, betMoney + 1000);
                    betService.addParticipant(betId, user, betMoney, isBetWin);
                } catch (NotEnoughMoneyException e) {
                    channelMessageSender.sendMessage(textChannel,
                            String.format("돈이 부족합니다!\n소지금 : %d원\n필요금 : %d원",
                                    e.getPossessionMoney(),
                                    e.getRequiredMoney()));
                } catch (ArithmeticException e) {
                    channelErrorMessageSender.sendMessage(textChannel, e.getMessage());
                } catch (UnknownBetIdException e) {
                    channelMessageSender.sendMessage(textChannel, String.format("존재하지 않는 Id(%s)입니다", e.getId()));
                }
                channelMessageSender.sendMessage(textChannel, sender.getAsMention() + "배팅에 성공적으로 참여되셧습니다!");
            }
        });
    }
}

@AllArgsConstructor
enum DisplayWinOrLose {
    WIN("승리"), LOSE("패배");

    @Getter
    private final String display;
}

