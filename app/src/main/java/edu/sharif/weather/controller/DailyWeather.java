package edu.sharif.weather.controller;

public class DailyWeather {

    private int temp;
    private int feelsLike;
    private int windSpeed;
    private int humidity;

    public DailyWeather(int temp, int feelsLike, int windSpeed, int humidity) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }
}
