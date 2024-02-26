package com.example.howstheweather.domain.usecases

import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.model.ForecastWeather
import com.example.howstheweather.domain.repository.WeatherRepository
import com.example.howstheweather.utils.Response
import javax.inject.Inject

class GetWeatherForecastUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: String, lon: String): Response<List<ForecastWeather>> {
        return weatherRepository.getWeatherForecast(lat, lon)
    }
}