package com.example.howstheweather.domain.model

data class CurrentWeather(
    val latLong: Pair<String, String>,
    val weatherTitle: String,
    val weatherDescription: String,
    val weatherIconUrl: String,
    val temp: String,
    val tempFeelsLike: String,
    val minTemp: String,
    val maxTemp: String,
    val windSpeed: String
)
