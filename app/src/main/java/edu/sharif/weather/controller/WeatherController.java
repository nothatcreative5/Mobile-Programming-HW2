package edu.sharif.weather.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

import edu.sharif.weather.BuildConfig;
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
            urlBuilder.addQueryParameter("appid", BuildConfig.OPEN_WEATHER_API_KEY);
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
            urlBuilder.addQueryParameter("access_token", BuildConfig.MAP_BOX_API_KEY);
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

    public Bitmap getWeatherIcon(String iconCode) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl
                    .parse("https://openweathermap.org/img/wn/" + iconCode + "@2x.png")
                    .newBuilder();
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
