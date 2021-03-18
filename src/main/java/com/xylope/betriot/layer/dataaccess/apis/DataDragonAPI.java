package com.xylope.betriot.layer.dataaccess.apis;

import com.xylope.betriot.layer.dataaccess.RiotAPI;

public interface DataDragonAPI extends RiotAPI {
    String getVersionLast();
    String getProfileIconURL(int iconId);
    String getChampionIdByKey(long key);
    String getChampionImageUrlById(String id);
    String getRuneImageUrlById(int id);
}
