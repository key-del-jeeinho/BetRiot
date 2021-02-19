package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xylope.betriot.data.riotdata.SummonerDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RiotAPITemplate {
    public static  <T> T getData(String urlStr, RiotAPICallback<T> callback) {
        URL url;
        BufferedReader br = null;

        try {
            url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));

            JsonObject k = JsonParser.parseReader(br).getAsJsonObject();

            return callback.getRiotDataObject(k);
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
}
