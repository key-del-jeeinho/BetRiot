package com.xylope.betriot.layer.service.user.apis;

import com.xylope.betriot.exception.ZeroChampionMasteryPointException;
import com.xylope.betriot.layer.dataaccess.riotdata.ChampionMasteryDto;
import com.xylope.betriot.layer.dataaccess.riotdata.SummonerDto;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class UserSummonerAPIImpl extends UserSummonerAPI{
    @Override
    public MessageEmbed getUserProfileMessage(SummonerDto user, Color embedColor) {
        return new EmbedBuilder()
                .setColor(embedColor)
                .setThumbnail(getUserThumbnailUrl(user))
                .setTitle(user.getName() + "님의 프로필")
                .addField("소환사님의 정보",
                        String.format("이름 : %s\n레벨 : %d",
                                user.getName(),
                                user.getSummonerLevel()), false)
                .build();
    }

    //TODO Xylope | 아직 테스트되지 않은 클래스 및 메서드 | 2021.03.07
    @Override
    public String getUserThumbnailUrl(SummonerDto user) {
        String riotId = user.getId();
        ChampionMasteryDto mastery;
        try {
            mastery = championMasteryAPI.getChampionMasteryTopById(riotId);
        } catch (ZeroChampionMasteryPointException e) {
            int profileIcon = user.getProfileIconId();
            return dataDragonAPI.getProfileIconURL(profileIcon);
        }
        return dataDragonAPI.getChampionImageUrlById(dataDragonAPI.getChampionIdByKey(mastery.getChampionId()));
    }

    @Deprecated
    @Override
    public void setRiotApiKey(String riotKey) {}
}
