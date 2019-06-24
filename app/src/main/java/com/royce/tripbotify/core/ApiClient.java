package com.royce.tripbotify.core;

import android.util.Log;

import com.royce.tripbotify.utils.AppConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ApiClient {

    private static OkHttpClient okHttpClient;
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private String[] unplashKeys = {"0f4cdc7b591d7625da76d059fcb376170b7cf552a35d65a8081b4d24c319c77f"};

    private static final String URL = AppConstants.IS_PRODUCTION ? AppConstants.API_PRODUCTION : AppConstants.API_TEST;

    private static synchronized OkHttpClient getInstance() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient();
        }
        return okHttpClient;
    }

    public static String getUnsplashImageURL(String searchTerm) {
        try {
            Request request = new Request.Builder()
                    .url("https://api.unsplash.com/search/photos?client_id=0f4cdc7b591d7625da76d059fcb376170b7cf552a35d65a8081b4d24c319c77f&query=" + searchTerm)
                    //.addHeader("x-wiz-device-type", "ANDROID")
                    .build();
            Response response = getInstance().newCall(request).execute();
            if (response.code() == 200) {
                JSONObject result = new JSONObject(response.body().string());
                JSONArray results = result.getJSONArray("results");
                return results.getJSONObject(0).getJSONObject("urls").getString("regular");
            }
            return "";
        } catch (IOException | JSONException e) {
            Log.i(AppConstants.LOG_TAG, e.getLocalizedMessage());
            return "";
        }
    }


    public static Response get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                // .addHeader("x-wiz-device-type", "ANDROID")
                .build();

        return getInstance().newCall(request).execute();
    }

    public static Response getWithKey(String url, String key) throws IOException {
        Request request = new Request.Builder()
                .url(URL + url)
                .addHeader("Authorization", "Bearer " + key)
                //.addHeader("x-wiz-device-type", "ANDROID")
                .build();

        return getInstance().newCall(request).execute();
    }

    public static Response getWithKeyAndParams(String url, String key, Map<String, String> params) throws IOException {

        HttpUrl.Builder builder = HttpUrl.parse(URL + url).newBuilder();
        for (String mapKey : params.keySet())
            builder.addQueryParameter(mapKey, params.get(mapKey));
        Request request = new Request.Builder()
                .url(builder.build().toString())
                .addHeader("Authorization", "Bearer " + key)
                // .addHeader("x-wiz-device-type", "ANDROID")
                // .addHeader("x-wiz-device-id", fcm)
                .build();

        return getInstance().newCall(request).execute();
    }

    public static Response post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(URL + url)
                .addHeader("x-wiz-device-type", "ANDROID")
                .post(body)
                .build();
        return getInstance().newCall(request).execute();
    }

    public static Response delete(String url, String key) throws IOException {
        Request request = new Request.Builder()
                .url(URL + url)
                .addHeader("x-wiz-device-type", "ANDROID")
                .addHeader("Authorization", "Bearer " + key)
                .delete()
                .build();
        return getInstance().newCall(request).execute();
    }

    public static Response patch(String url, String json, String key) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(URL + url)
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("x-wiz-device-type", "ANDROID")
                .patch(body)
                .build();
        return getInstance().newCall(request).execute();
    }

    public static Response postWithHeader(String url, String json, String key) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(URL + url)
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("x-wiz-device-type", "ANDROID")
                .post(body)
                .build();
        return getInstance().newCall(request).execute();
    }

    public static Response postEmptyParamsWithHeader(String url, String key) throws IOException {
        Request request = new Request.Builder()
                .url(URL + url)
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("x-wiz-device-type", "ANDROID")
                .post(RequestBody.create(null, new byte[0]))
                .build();
        return getInstance().newCall(request).execute();
    }

    public static String getAmadeusAccessToken() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("grant_type", "client_credentials");
            jsonObject.put("client_id", "hiQb0uhPPNuSGtm5ssxapQ2K8nHvNJ3n");
            jsonObject.put("client_secret", "vanIVyyEeaFqgYGL");
            RequestBody body = RequestBody.create(JSON, jsonObject.toString());
            Request request = new Request.Builder()
                    .url(URL + "security/oauth2/token")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .post(body)
                    .build();
            Response response = getInstance().newCall(request).execute();
            if (response.code() == 200 && response.body() != null) {
                JSONObject resp = new JSONObject(response.body().string());
                return resp.getString("access_token");
            } else
                Log.i(AppConstants.LOG_TAG, response.code() + response.message());
            return "";
        } catch (JSONException | IOException e) {
            Log.i(AppConstants.LOG_TAG, e.getLocalizedMessage());
            return "";
        }
    }

    /*@SuppressWarnings("unused")
    public static Response postEXOTEL(String from, String to) throws IOException {
        Request request = new Request.Builder()
                .url(AppConstants.EXOTEL_1 + from + AppConstants.EXOTEL_2 + to + AppConstants.EXOTEL_3)
                .post(RequestBody.create(null, new byte[0]))
                .addHeader("Accept-Type", "application/json")
                .addHeader("Authorization", "Basic d2l6Y291bnNlbDpmNWNhNDVhODc2YzQyM2U5YTk0ZjNkNmU3NDMwMzk1YzY2NzU4YjBh")
                .build();
        return new OkHttpClient().newCall(request).execute();
    }*/

    /*public static Response getCourt(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        // was used for court case status
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        for (String mapKey : params.keySet())
            builder.addQueryParameter(mapKey, params.get(mapKey));
        Request request = new Request.Builder()
                .url(builder.build().toString())
                .build();

        return getInstance().newCall(request).execute();
    }*/

}

