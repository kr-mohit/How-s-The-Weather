package com.example.howstheweather.data.model

import com.example.howstheweather.domain.model.CurrentWeather

data class CurrentWeatherDTO(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Coord,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

fun CurrentWeatherDTO.toDomainCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        latLong = Pair(this.coord.lat.toString(), this.coord.lon.toString()),
        weatherTitle = this.weather[0].main,
        weatherDescription = this.weather[0].description,
        weatherIconUrl = "https://openweathermap.org/img/wn/" + this.weather[0].icon + "@2x.png",
        temp = this.main.temp.toString(),
        tempFeelsLike = this.main.feels_like.toString(),
        minTemp = this.main.temp_min.toString(),
        maxTemp = this.main.temp_max.toString(),
        windSpeed = this.wind.speed.toString()
    )
}