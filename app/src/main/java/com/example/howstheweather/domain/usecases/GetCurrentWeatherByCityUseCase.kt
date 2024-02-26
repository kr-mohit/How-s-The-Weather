package com.example.howstheweather.domain.usecases

import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.repository.WeatherRepository
import com.example.howstheweather.utils.Response
import javax.inject.Inject

class GetCurrentWeatherByCityUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(city: String): Response<CurrentWeather> {
        return weatherRepository.getCurrentWeatherByCity(city)
    }
}