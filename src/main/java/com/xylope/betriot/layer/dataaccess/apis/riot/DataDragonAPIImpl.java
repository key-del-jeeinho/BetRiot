package com.xylope.betriot.layer.dataaccess.apis.riot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.merakianalytics.orianna.Orianna;
import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.layer.dataaccess.RiotAPI;
import com.xylope.betriot.layer.dataaccess.RiotAPITemplate;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DataDragonAPIImpl implements DataDragonAPI {
    @Setter @Getter
    private String riotApiKey;

    @Override
    public String getVersionLast() {
        return RiotAPITemplate.getData(RiotAPI.DATA_DRAGON_ROOT_URL + "/api/versions.json", (JsonArray k) -> k.get(0).getAsString());
    }

    @Override
    public String getProfileIconURL(int iconId) {
        return RiotAPI.DATA_DRAGON_ROOT_URL + "/cdn/" + getVersionLast() + "/img/profileicon/" + iconId + ".png";
    }

    @Override
    public String getChampionIdByKey(long key) {
        JsonObject root = RiotAPITemplate.getData(RiotAPI.DATA_DRAGON_ROOT_URL + "/cdn/" + getVersionLast() + "/data/en_US/champion.json",
                (JsonObject k) -> k);
        JsonObject data = root.getAsJsonObject("data");
        Set<String> championIds = data.keySet();
        for(String championId : championIds) {
            JsonObject obj = data.getAsJsonObject(championId);
            if(obj.get("key").getAsLong() == key) {
                return obj.get("id").getAsString();
            }
        }
        throw new DataNotFoundException("unknown champion id : " + key);
    }

    @Override
    public String getChampionImageUrlById(String id) {
        return RiotAPI.DATA_DRAGON_ROOT_URL + "/cdn/" + getVersionLast() + "/img/champion/" + id + ".png";
    }

    @Override
    public String getRuneImageUrlById(int id) {
        AtomicReference<String> url = new AtomicReference<>();
        AtomicBoolean isDataFound = new AtomicBoolean(false);
        Orianna.getRunes().forEach(
                (rune) -> {
                    if (rune.getId() == id) {
                        url.set(rune.getImage().getURL());
                        isDataFound.set(false);
                    }
                }
        );
        if(isDataFound.get())
            return url.get();
        throw new DataNotFoundException("unknown rune id : " + id);
    }

}
