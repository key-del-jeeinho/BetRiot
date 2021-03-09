package com.xylope.betriot.layer.service.user.apis;

import com.xylope.betriot.layer.dataaccess.apis.ChampionMasteryAPI;
import com.xylope.betriot.layer.dataaccess.apis.DataDragonAPI;
import com.xylope.betriot.layer.dataaccess.apis.SummonerAPI;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import lombok.Setter;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;
import java.util.List;

public abstract class UserSummonerAPI implements SummonerAPI {
    @Setter
    protected SummonerAPI summonerAPI;
    @Setter
    protected DataDragonAPI dataDragonAPI;
    @Setter
    protected ChampionMasteryAPI championMasteryAPI;

    public abstract MessageEmbed getUserProfileMessage(SummonerDto user, Color embedColor);

    //TODO Xylope | 아직 테스트되지 않은 클래스 및 메서드 | 2021.03.07
    public abstract String getUserThumbnailUrl(SummonerDto user);

    @Override
    public SummonerDto getByName(String name) {
        return summonerAPI.getByName(name);
    }

    @Override
    public SummonerDto getById(String id) {
        return summonerAPI.getById(id);
    }

    @Override
    public SummonerDto getByPuuid(String puuid) {
        return summonerAPI.getByPuuid(puuid);
    }

    @Override
    public SummonerDto getByAccountId(String accountId) {
        return summonerAPI.getByAccountId(accountId);
    }

    @Override
    public List<SummonerDto> getByMatch(String matchId) {
        return summonerAPI.getByMatch(matchId);
    }
}
