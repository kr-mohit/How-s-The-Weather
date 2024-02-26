package com.example.howstheweather.domain.usecases

import com.example.howstheweather.domain.model.LocationData
import com.example.howstheweather.domain.repository.WeatherRepository
import com.example.howstheweather.utils.Response
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: String, lon: String): Response<LocationData> {
        return weatherRepository.getLocationFromLatLon(lat, lon)
    }
}