package com.xylope.betriot.layer.dataaccess;

import java.net.URL;

public interface DataDragonAPI extends RiotAPI{
    String getVersionLast();
    URL getProfileIconURL(int iconId);
}
