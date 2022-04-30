package edu.sharif.weather;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

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
        try {
            JSONObject x = wc.getWeatherByGeoLocation("33.44", "-94.04");
            System.out.println(x.toString());
        } catch (IOException e) {
            System.out.println(1111);
            e.printStackTrace();
        }
        assertEquals(4, 2 + 2);
    }

    @Test
    public void location_isCorrect() {
        WeatherController wc = new WeatherController();
        try {
            HashMap<String, String> x = wc.getGeoLocation("Los Angeles");
            System.out.println(x.get("lon"));
            System.out.println(x.get("lat"));
        } catch (IOException e) {
            System.out.println(1111);
            e.printStackTrace();
        }
        assertEquals(4, 2 + 2);
    }
}