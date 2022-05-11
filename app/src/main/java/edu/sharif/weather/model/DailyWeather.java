package edu.sharif.weather.model;

public class DailyWeather {

    private final double temp;
    private final double feelsLike;
    private final double windSpeed;
    private final int humidity;
    private String cityName;
    private final String description;
    private final String icon;

    public DailyWeather(double temp, double feelsLike, double windSpeed, int humidity, String description, String icon) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.description = description;
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public double getFeelsLike() {
        return feelsLike;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public int getHumidity() {
        return humidity;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
