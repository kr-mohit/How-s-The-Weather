package com.example.howstheweather.data.repository

import com.example.howstheweather.data.model.toDomainCurrentWeather
import com.example.howstheweather.data.model.toDomainForecastWeather
import com.example.howstheweather.data.model.toDomainLocation
import com.example.howstheweather.data.remote.WeatherAPI
import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.model.ForecastWeather
import com.example.howstheweather.domain.model.LocationData
import com.example.howstheweather.domain.repository.WeatherRepository
import com.example.howstheweather.utils.Response
import org.json.JSONObject
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherAPI: WeatherAPI
): WeatherRepository {
    override suspend fun getCurrentWeatherByLatLon(lat: String, lon: String): Response<CurrentWeather> {
        return try {
            val response = weatherAPI.getCurrentWeatherByLatLon(lat, lon, "metric")
            if (response.isSuccessful) {
                Response.Success(response.body()?.toDomainCurrentWeather())
            } else {
                Response.Error("Something Went Wrong")
            }
        } catch (e: Exception) {
            Response.Error("Something Went Wrong")
        }
    }

    override suspend fun getWeatherForecast(lat: String, lon: String): Response<List<ForecastWeather>> {
        return try {
            val response = weatherAPI.getWeatherForecast(lat, lon, "metric")
            if (response.isSuccessful) {
                Response.Success(response.body()?.list?.map { it.toDomainForecastWeather() })
            } else {
                Response.Error("Something Went Wrong")
            }
        } catch (e: Exception) {
            Response.Error("Something Went Wrong")
        }
    }

    override suspend fun getCurrentWeatherByCity(city: String): Response<CurrentWeather> {
        return try {
            val response = weatherAPI.getCurrentWeatherByCity(city, "metric")
            if (response.isSuccessful) {
                Response.Success(response.body()?.toDomainCurrentWeather())
            } else {
                val jsonObject = JSONObject(response.errorBody()!!.string())
                val errorMessage = jsonObject.getString("message")
                Response.Error(errorMessage ?: "Something Went Wrong")
            }
        } catch (e: Exception) {
            Response.Error("Something Went Wrong")
        }
    }

    override suspend fun getLocationFromLatLon(lat: String, lon: String): Response<LocationData> {
        return try {
            val response = weatherAPI.getLocationFromLatLon(lat, lon)
            if (response.isSuccessful) {
                Response.Success(response.body()?.get(0)?.toDomainLocation())
            } else {
                Response.Error("Something Went Wrong")
            }
        } catch (e: Exception) {
            Response.Error("Something Went Wrong")
        }
    }
}