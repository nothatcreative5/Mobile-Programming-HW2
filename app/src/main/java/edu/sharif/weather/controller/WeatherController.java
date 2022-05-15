package edu.sharif.weather.controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import edu.sharif.weather.BuildConfig;
import edu.sharif.weather.model.DailyWeather;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WeatherController {
    private final OkHttpClient client;

    public WeatherController() {
        client = new OkHttpClient();
    }

    public ArrayList<DailyWeather> getWeatherByGeoLocation(String lat, String lon) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.openweathermap.org/data/2.5/onecall").newBuilder();
            urlBuilder.addQueryParameter("lat", lat);
            urlBuilder.addQueryParameter("lon", lon);
            urlBuilder.addQueryParameter("exclude", "minutely,hourly");
            urlBuilder.addQueryParameter("units", "metric");
            urlBuilder.addQueryParameter("appid", BuildConfig.OPEN_WEATHER_API_KEY);
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            String body = Objects.requireNonNull(response.body()).string();

            JSONObject obj = new JSONObject(body);
            ArrayList<DailyWeather> output = new ArrayList<>();
            JSONArray dailyWeathers = obj.getJSONArray("daily");
            for (int i = 0; i < 8; i++) {
                JSONObject weather = dailyWeathers.getJSONObject(i);
                output.add(new DailyWeather(
                        weather.getJSONObject("temp").getDouble("day"),
                        weather.getJSONObject("feels_like").getDouble("day"),
                        weather.getDouble("wind_speed"),
                        weather.getInt("humidity"),
                        weather.getJSONArray("weather").getJSONObject(0).getString("description"),
                        weather.getJSONArray("weather").getJSONObject(0).getString("icon")
                ));
            }
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

            //todo(sadegh)
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

    public ArrayList<DailyWeather> getWeatherByLocationName(String locationName) {
        HashMap<String, String> location = getGeoLocation(locationName);
        try {
            return getWeatherByGeoLocation(location.get("lat"), location.get("lon"));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getCityName(String lon, String lat) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.mapbox.com/geocoding/v5/mapbox.places/" + lon + "," + lat + ".json").newBuilder();
            urlBuilder.addQueryParameter("access_token", BuildConfig.MAP_BOX_API_KEY);
            urlBuilder.addQueryParameter("types", "place");
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            //todo(sadegh)
            Log.d("sadegh","kir");
            Response response = client.newCall(request).execute();
            String body = Objects.requireNonNull(response.body()).string();
            Log.d("sadegh",body);
            JSONObject obj = new JSONObject(body);
            String cityName = obj.getJSONArray("features").getJSONObject(0).getString("text");
            return cityName;
        } catch (IOException | JSONException e) {
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
