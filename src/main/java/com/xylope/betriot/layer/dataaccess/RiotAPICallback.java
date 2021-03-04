package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;

@FunctionalInterface
public interface RiotAPICallback<T, T2> {
    T getRiotDataObject(T2 k) throws IOException;
    interface JsonObjectCallback<T> extends RiotAPICallback<T, JsonObject>{}
    interface JsonArrayCallback<T> extends RiotAPICallback<T, JsonArray>{}
    interface StringCallback<T> extends RiotAPICallback<T, String>{}
}
