package com.xylope.betriot.layer.service.user;

import com.xylope.betriot.layer.dataaccess.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class UserSummonerAPI implements SummonerAPI {
    @Setter
    private SummonerAPI api;

    public MessageEmbed getUserProfileMessage(String discordId) {

    }

    public String getUserThumbnailUrl(String discordId) {
        return null;
    }

    @Override
    public void setRiotApiKey(String riotKey) {
        api.setRiotApiKey(riotKey);
    }

    @Override
    public SummonerDto getByName(String name) {
        return api.getByName(name);
    }

    @Override
    public SummonerDto getById(String id) {
        return api.getById(id);
    }

    @Override
    public SummonerDto getByPuuid(String puuid) {
        return api.getByPuuid(puuid);
    }

    @Override
    public SummonerDto getByAccountId(String accountId) {
        return api.getByAccountId(accountId);
    }

    @Override
    public List<SummonerDto> getByMatch(String matchId) {
        return api.getByMatch(matchId);
    }
}
