package com.example.howstheweather.data.model

data class WeatherForecastDTO(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<ForecastWeatherDTO>,
    val message: Int
)