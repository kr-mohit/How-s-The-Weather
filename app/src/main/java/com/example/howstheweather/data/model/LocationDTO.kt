package com.example.howstheweather.data.model

import com.example.howstheweather.domain.model.LocationData

data class LocationDTO(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)

fun LocationDTO.toDomainLocation(): LocationData {
    return LocationData(
        name = this.name,
        country = this.country,
        lat = this.lat.toString(),
        lon = this.lon.toString()
    )
}