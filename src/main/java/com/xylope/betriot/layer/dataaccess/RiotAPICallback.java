package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonObject;

@FunctionalInterface
public interface RiotAPICallback<T> {
    T getRiotDataObject(JsonObject k);
}
