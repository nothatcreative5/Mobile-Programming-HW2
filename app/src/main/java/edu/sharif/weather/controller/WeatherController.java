package edu.sharif.weather.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherController {
    private final OkHttpClient client;

    public WeatherController() {
        client = new OkHttpClient();
    }

    public JSONObject getWeatherByGeoLocation(String lat, String lon) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.openweathermap.org/data/2.5/onecall").newBuilder();
            urlBuilder.addQueryParameter("lat", lat);
            urlBuilder.addQueryParameter("lon", lon);
            urlBuilder.addQueryParameter("exclude", "minutely,hourly,daily");
            urlBuilder.addQueryParameter("units", "metric");
            urlBuilder.addQueryParameter("appid", "da791b2b3446f7ecaaf66562a11f07cd");
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            String body = Objects.requireNonNull(response.body()).string();

            JSONObject obj = new JSONObject(body);
            JSONObject currentWeather = obj.getJSONObject("current");
            JSONObject output = new JSONObject();
            output.put("temp", currentWeather.getDouble("temp"));
            output.put("feels_like", currentWeather.getDouble("feels_like"));
            output.put("wind_speed", currentWeather.getDouble("wind_speed"));
            output.put("humidity", currentWeather.getInt("humidity"));
            JSONObject weather = new JSONObject();
            weather.put("description", currentWeather.getJSONArray("weather").getJSONObject(0).getString("description"));
            weather.put("icon", currentWeather.getJSONArray("weather").getJSONObject(0).getString("icon"));
            output.put("weather", weather);
            return output;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private HashMap<String, String> getGeoLocation(String locationName) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.mapbox.com/geocoding/v5/mapbox.places/" + locationName + ".json").newBuilder();
            urlBuilder.addQueryParameter("access_token", "pk.eyJ1Ijoic2FkZWdoLW1hamlkaSIsImEiOiJjbDJrdjE3Y3EwZ3VxM2pvOGgxNWNmazhpIn0.TKYyVlsskmF5F5HFkwK5zg");
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            String body = Objects.requireNonNull(response.body()).string();

            JSONObject obj = new JSONObject(body);
            JSONArray geoLocArr = obj.getJSONArray("features").getJSONObject(0).getJSONArray("center");
            HashMap<String, String> geoLoc = new HashMap<>();
            geoLoc.put("lat", String.valueOf(geoLocArr.getDouble(1)));
            geoLoc.put("lon", String.valueOf(geoLocArr.getDouble(0)));
            return geoLoc;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JSONObject getWeatherByLocationName(String locationName) {
        HashMap<String, String> location = getGeoLocation(locationName);
        try {
            return getWeatherByGeoLocation(location.get("lat"), location.get("lon"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
