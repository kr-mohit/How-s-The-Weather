package com.example.howstheweather.domain.usecases

import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.repository.WeatherRepository
import com.example.howstheweather.utils.Response
import javax.inject.Inject

class GetCurrentWeatherByLatLonUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: String, lon: String): Response<CurrentWeather> {
        return weatherRepository.getCurrentWeatherByLatLon(lat, lon)
    }
}