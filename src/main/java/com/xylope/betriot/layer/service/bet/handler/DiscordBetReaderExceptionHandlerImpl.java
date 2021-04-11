package com.xylope.betriot.layer.service.bet.handler;

import com.xylope.betriot.exception.bet.WrongArgumentException;
import com.xylope.betriot.layer.service.bet.BetService;
import com.xylope.betriot.exception.bet.BetAlreadyCreatedException;
import com.xylope.betriot.layer.service.bet.reader.Action;
import com.xylope.betriot.layer.service.bet.reader.BetReader;
import com.xylope.betriot.layer.logic.discord.message.ChannelErrorMessageSender;
import com.xylope.betriot.layer.logic.discord.message.ChannelMessageSenderImpl;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.entities.TextChannel;

@RequiredArgsConstructor
public class DiscordBetReaderExceptionHandlerImpl extends DiscordBetReaderExceptionHandler {
    @Setter
    private TextChannel textChannel;
    private final ChannelErrorMessageSender channelErrorMessageSender;
    private final ChannelMessageSenderImpl channelMessageSenderImpl;

    @Override
    public void handling(BetReader<String[]> reader, BetService service, Action action, String... input) {
        try {
            super.read(reader, service, action, input);
        } catch (BetAlreadyCreatedException e) {
            channelMessageSenderImpl.sendMessage(textChannel, "이미 %s님의 배팅이 존재합니다");
        } catch (WrongArgumentException e) {
            int argIdx = e.getArgumentIdx();

            if(action.equals(Action.BET_OPEN)) {
                if (argIdx == 0) {
                    String errMessage;
                    if((errMessage = e.getCause().getMessage()) == null)
                        errMessage = e.getMessage();
                    channelErrorMessageSender.sendMessage(textChannel, errMessage);
                }
            }

            if(action.equals(Action.BETTING_PARTICIPATION)) {
                switch (argIdx) {
                    case 0:
                        String errMessage;
                        if((errMessage = e.getCause().getMessage()) == null)
                            errMessage = e.getMessage();
                        channelErrorMessageSender.sendMessage(textChannel, errMessage);
                        break;
                    case 1:
                        channelMessageSenderImpl.sendMessage(textChannel, "배팅 아이디는 정수값이어야합니다!");
                        break;
                    case 2:
                        channelMessageSenderImpl.sendMessage(textChannel, "배팅은 승리/패배 에만 가능합니다");
                        break;
                    case 3:
                        channelMessageSenderImpl.sendMessage(textChannel, "배팅에 거실 돈은 정수값이어야합니다!");
                        break;
                }
            }
        } catch (Exception e) { //예상하지 못한 예외가 나왔을경우 이를 오류상황이라 인식한다
            String errMessage = e.getMessage();
            if (e.getCause() != null) {
                if ((errMessage = e.getCause().getMessage()) != null) {
                    errMessage = e.getCause().getMessage();
                }
            }
            channelErrorMessageSender.sendMessage(textChannel, errMessage);
        }
    }
}
