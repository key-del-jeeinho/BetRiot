package com.xylope.betriot.layer.dataaccess;

public interface DataDragonAPI extends RiotAPI{
    String getVersionLast();
    String getProfileIconURL(int iconId);
    String getChampionIdByKey(long key);
    String getChampionImageUrlById(String id);
}
