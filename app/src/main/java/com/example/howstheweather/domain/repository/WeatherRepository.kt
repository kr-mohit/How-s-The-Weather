package com.example.howstheweather.domain.repository

import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.model.ForecastWeather
import com.example.howstheweather.domain.model.LocationData
import com.example.howstheweather.utils.Response

interface WeatherRepository {

    suspend fun getCurrentWeatherByLatLon(lat: String, lon: String): Response<CurrentWeather>

    suspend fun getWeatherForecast(lat: String, lon: String): Response<List<ForecastWeather>>

    suspend fun getCurrentWeatherByCity(city: String): Response<CurrentWeather>

    suspend fun getLocationFromLatLon(lat: String, lon: String): Response<LocationData>

}