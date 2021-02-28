package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonArray;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

public class DataDragonAPIImpl implements DataDragonAPI {
    @Setter @Getter
    private String riotApiKey;

    @Override
    public String getVersionLast() {
        return RiotAPITemplate.getData(RiotAPI.dataDragonRootUrl + "/api/versions.json", (JsonArray k) -> {
            return k.get(0).getAsString();
        });
    }

    @Override
    public URL getProfileIconURL(int iconId) {
        return new URL(RiotAPI.dataDragonRootUrl + "/cdn/10.11.1/img/profileicon/" + iconId + ".png");
    }


}
