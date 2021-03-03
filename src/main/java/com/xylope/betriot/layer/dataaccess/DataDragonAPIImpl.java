package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.layer.dataaccess.datadragondata.ChampionDto;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URL;

public class DataDragonAPIImpl implements DataDragonAPI {
    @Setter @Getter
    private String riotApiKey;

    @Override
    public String getVersionLast() {
        return RiotAPITemplate.getData(RiotAPI.dataDragonRootUrl + "/api/versions.json", (JsonArray k) -> k.get(0).getAsString());
    }

    @Override
    public URL getProfileIconURL(int iconId) throws MalformedURLException {
        return new URL(RiotAPI.dataDragonRootUrl + "/cdn/" + getVersionLast() + "/img/profileicon/" + iconId + ".png");
    }

    @Override
    public String getChampionIdByKey(int id) {
        JsonObject root = RiotAPITemplate.getData(RiotAPI.dataDragonRootUrl + "/cdn/" + getVersionLast() + "/data/en_US/champion.json",
                (JsonObject k) -> k);
        JsonArray data = root.getAsJsonArray("data");
        for(JsonElement element : data) {
            JsonObject championData = element.getAsJsonObject();
            if(championData.get("key").getAsInt() == id) {
                return championData.get("id").getAsString();
            }
        }
        throw new DataNotFoundException();
    }

}
