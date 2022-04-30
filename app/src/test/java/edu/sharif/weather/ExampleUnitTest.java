package edu.sharif.weather;

import static org.junit.Assert.assertEquals;

import android.graphics.Bitmap;

import org.json.JSONObject;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

import edu.sharif.weather.controller.WeatherController;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void weather_isCorrect() {
        WeatherController wc = new WeatherController();
        JSONObject x = wc.getWeatherByGeoLocation("33.44", "-94.04");
        System.out.println(x.toString());
        assertEquals(4, 2 + 2);
    }

    @Test
    public void location_isCorrect() {
        WeatherController wc = new WeatherController();
        JSONObject x = wc.getWeatherByLocationName("Tehran");
        System.out.println(x.toString());
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