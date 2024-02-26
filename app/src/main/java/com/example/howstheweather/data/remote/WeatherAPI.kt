package com.example.howstheweather.data.remote

import com.example.howstheweather.data.model.CurrentWeatherDTO
import com.example.howstheweather.data.model.LocationDTO
import com.example.howstheweather.data.model.WeatherForecastDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByLatLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Response<CurrentWeatherDTO>

    @GET("data/2.5/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String
    ): Response<WeatherForecastDTO>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByCity(
        @Query("q") city: String,
        @Query("units") units: String
    ): Response<CurrentWeatherDTO>

    @GET("geo/1.0/reverse")
    suspend fun getLocationFromLatLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String
    ): Response<List<LocationDTO>>
}