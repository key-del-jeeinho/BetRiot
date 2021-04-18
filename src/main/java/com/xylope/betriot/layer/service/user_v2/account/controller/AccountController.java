package com.xylope.betriot.layer.service.user_v2.account.controller;

import com.xylope.betriot.layer.dataaccess.apis.discord.JdaAPI;
import com.xylope.betriot.layer.dataaccess.apis.riot.DataDragonAPI;
import com.xylope.betriot.layer.dataaccess.apis.riot.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import com.xylope.betriot.layer.service.user_v2.account.model.NewRegisterAccount;
import com.xylope.betriot.layer.service.user_v2.account.model.NewRegisterAccountDto;
import com.xylope.betriot.layer.service.user_v2.account.model.NewRegisterAccountProgress;
import com.xylope.betriot.layer.service.user_v2.account.model.NewRegisterAccountQueue;
import com.xylope.betriot.layer.service.user_v2.account.view.AccountView;
import lombok.AllArgsConstructor;
import net.dv8tion.jda.api.entities.PrivateChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@AllArgsConstructor
public class AccountController {
    private final NewRegisterAccountQueue model;
    private final AccountView view;
    private final JdaAPI jdaApi;
    private final SummonerAPI summonerAPI;
    private final DataDragonAPI dataDragonAPI;

    public void createAccount(NewRegisterAccount account) {
        model.addAccount(account);
    }

    public boolean checkProgress(long accountId, NewRegisterAccountProgress progress) {
        return model.checkProgress(accountId, progress);
    }

    public void nextStep(long accountId) {
        model.nextStep(accountId);
    }


    public void policyCheck(long accountId) {
        long discordId = model.getAccount(accountId).getDiscordId();
        PrivateChannel pc = jdaApi.getPrivateChannelByUserId(discordId);
        long policyMessageId = view.sendPolicyView(pc);
        model.setPolicyMessageId(accountId, policyMessageId);
    }

    public boolean checkIsPolicyChecked(long accountId) {
        return model.checkIsPolicyChecked(accountId);
    }

    //만약, 유저가 약관 동의/비동의시 이에대한 처리를 하는 로직, 유저의 약관동의여부를 반환한다.
    public boolean policyLogic(long accountId) {
        boolean isUserAcceptPolicy = model.getIsPolicyAccept(accountId);
        NewRegisterAccountDto account = model.getAccount(accountId);
        PrivateChannel pc = jdaApi.getPrivateChannelByUserId(account.getDiscordId());
        if(isUserAcceptPolicy) {
            view.sendUserAcceptPolicyView(pc);
        } else {
            view.sendUserDenyPolicyView(pc);
        }

        return isUserAcceptPolicy;
    }

    public void authorizeRiotName(long accountId) {
        long discordId = model.getAccount(accountId).getDiscordId();
        PrivateChannel pc = jdaApi.getPrivateChannelByUserId(discordId);
        long messageId = view.sendAuthorizeRiotNameView(pc);
        model.setRiotAuthorizeMessageId(accountId, messageId);
    }

    public boolean checkAuthorizeRiotName(long accountId) {
        return model.getAccount(accountId).getRiotName() != null;
    }

    public void authorizeRiotAccount(long accountId) {
        NewRegisterAccountDto account = model.getAccount(accountId);
        long discordId = account.getDiscordId();
        PrivateChannel pc = jdaApi.getPrivateChannelByUserId(discordId);

        System.out.println(account.getRiotName());
        System.out.println(summonerAPI);
        SummonerDto summoner = summonerAPI.getByName(account.getRiotName());
        System.out.println("summoner : " + summoner);
        String iconUrl = getAuthorizeIconUrl(summoner);
        System.out.println("icon url : " + iconUrl);

        long riotAuthorizeMessageId = view.sendAuthorizeRiotAccountView(pc, iconUrl);
        model.setRiotAuthorizeMessageId(accountId, riotAuthorizeMessageId);
    }

    public boolean checkAuthorizeRiotAccount(long accountId) {
        return model.checkAuthorizeRiotAccount(accountId);
    }

    private String getAuthorizeIconUrl(final SummonerDto summoner) {
        List<Integer> iconIdQueue = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5));
        iconIdQueue.removeIf(data -> (data == summoner.getProfileIconId()));
        int authorizeIconId = iconIdQueue.get(new Random().nextInt(iconIdQueue.size()));
        return dataDragonAPI.getProfileIconURL(authorizeIconId);
    }

    public void sendRegisterMessage(long accountId) {
        long discordId = model.getAccount(accountId).getDiscordId();
        PrivateChannel pc = jdaApi.getPrivateChannelByUserId(discordId);
        view.sendRegisterView(pc);
    }

    public void close(long accountId) {
        model.close(accountId);
    }
}
