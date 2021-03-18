package com.xylope.betriot.layer.dataaccess;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xylope.betriot.exception.DataNotFoundException;
import com.xylope.betriot.exception.RateLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RiotAPITemplate {
    public static <T> T getData(String urlStr, RiotAPICallback<T, ?> callback, Callback<T> logic) {
        BufferedReader br = null;
        try {
            URL url  = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            if (urlConnection.getResponseCode() != 200) {
                throw new ResponseStatusException(HttpStatus.valueOf(urlConnection.getResponseCode()));
            }
            br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8));
            return logic.doSomething(br, urlStr, callback);
        } catch (ResponseStatusException e) {
            HttpStatus status = e.getStatus();
            int statusCode = status.value();

            switch (RiotHttpStatus.getByCode(statusCode)) {
                case BAD_REQUEST:
                    break;
                case UNAUTHORIZED:
                    break;
                case FORBIDDEN:
                    break;
                case DATA_NOT_FOUND:
                    throw new DataNotFoundException();
                case METHOD_NOT_ALLOWED:
                    break;
                case UNSUPPORTED_MEDIA_TYPE:
                    break;
                case RATE_LIMIT_EXCEEDED:
                    throw new RateLimitExceededException();
                case INTERNAL_SERVER_ERROR:
                    break;
                case BAD_GATEWAY:
                    break;
                case SERVICE_UNAVAILABLE:
                    break;
                case GATEWAY_TIMEOUT:
                    break;
            }
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

    public static  <T> T getData(String urlStr, RiotAPICallback.JsonObjectCallback<T> callback) {
        Callback<T> logic = (br, urlStr1, callback1) -> {
            JsonObject k = JsonParser.parseReader(br).getAsJsonObject();
            return callback.getRiotDataObject(k);
        };

        return getData(urlStr, callback, logic);
    }

    public static  <T> T getData(String urlStr, RiotAPICallback.StringCallback<T> callback) {
        Callback<T> logic = (br, urlStr1, callback1) -> {
            String line;
            StringBuffer content = new StringBuffer();
            while ((line = br.readLine()) != null) {
                content.append(line);
            }
            return callback.getRiotDataObject(content.toString());
        };

        return getData(urlStr, callback, logic);
    }

    public static  <T> T getData(String urlStr, RiotAPICallback.JsonArrayCallback<T> callback) {
        Callback<T> logic = (br, urlStr1, callback1) -> {
            JsonArray k = JsonParser.parseReader(br).getAsJsonArray();
            return callback.getRiotDataObject(k);
        };

        return getData(urlStr, callback, logic);
    }

    @FunctionalInterface
    public interface Callback<T> {
        T doSomething(BufferedReader br, String urlStr, RiotAPICallback<T, ?> callback) throws IOException;
    }
}
