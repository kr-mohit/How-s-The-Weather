package com.example.howstheweather.data.model

import com.example.howstheweather.domain.model.ForecastWeather

data class ForecastWeatherDTO(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: MainX,
    val pop: Double,
    val rain: RainX,
    val sys: SysX,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

fun ForecastWeatherDTO.toDomainForecastWeather(): ForecastWeather {
    return ForecastWeather(
        date = this.dt_txt,
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
