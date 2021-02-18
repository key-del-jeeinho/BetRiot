package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xylope.betriot.BetRiotApplication;
import com.xylope.betriot.data.riotdata.SummonerDto;
import lombok.Getter;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SummonerV4SummonerDao implements SummonerDao{
    @Setter @Getter
    private String riotApiKey;
    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByName(String name) {
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getById(String id) {
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByPuuid(String puuid) {
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public SummonerDto getByAccountId(String accountId) {
        String urlStr;
        URL url;
        BufferedReader br = null;

        try {
            urlStr =
                    "https://kr.api.riotgames.com//lol/summoner/v4/summoners/by-account/" + accountId +  "?api_key=" + riotApiKey;
            url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            JsonObject k = JsonParser.parseReader(br).getAsJsonObject();
            return SummonerDto.builder()
                    .profileIconId(k.get("profileIconId").getAsInt())
                    .name(k.get("name").getAsString())
                    .puuid(k.get("puuid").getAsString())
                    .summonerLevel(k.get("summonerLevel").getAsLong())
                    .revisionDate(k.get("revisionDate").getAsLong())
                    .id(k.get("id").getAsString())
                    .accountId(k.get("accountId").getAsString()).build();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override//TODO 2021.02.19 | 미구현 | Xylope
    public List<SummonerDto> getByMatch(String matchId해당변수이름수정바람) {
        return null;
    }
}
