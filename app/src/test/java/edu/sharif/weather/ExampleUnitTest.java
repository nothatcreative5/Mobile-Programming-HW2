package edu.sharif.weather;

import static org.junit.Assert.assertEquals;

import android.graphics.Bitmap;

import org.json.JSONObject;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import edu.sharif.weather.controller.WeatherController;
import edu.sharif.weather.model.DailyWeather;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void weather_isCorrect() {
        WeatherController wc = new WeatherController();
        ArrayList<DailyWeather> x = wc.getWeatherByGeoLocation("33.44", "-94.04");
        assertEquals(4, 2 + 2);
    }

    @Test
    public void location_isCorrect() {
        WeatherController wc = new WeatherController();
        ArrayList<DailyWeather> x = wc.getWeatherByLocationName("Tehran");
        int i = 1;
        for (DailyWeather weather: x) {
            System.out.println("Day " + i);
            System.out.println("temp: " + weather.getTemp());
            System.out.println("feels_like: " + weather.getFeelsLike());
            System.out.println("wind_speed: " + weather.getWindSpeed());
            System.out.println("humidity: " + weather.getHumidity());
            System.out.println("description: " + weather.getDescription());
            System.out.println("icon: " + weather.getIcon());
            i++;
            System.out.println("=====================================================");
        }
        assertEquals(4, 2 + 2);
    }


//    @Test
//    public void icon_isCorrect() {
//        WeatherController wc = new WeatherController();
//        try (FileOutputStream out = new FileOutputStream("test.png")) {
//            Bitmap bmp = wc.getWeatherIcon("02d");
//            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}