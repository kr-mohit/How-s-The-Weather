package com.example.howstheweather.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.model.ForecastWeather
import com.example.howstheweather.domain.model.LocationData
import com.example.howstheweather.domain.usecases.GetCurrentWeatherByLatLonUseCase
import com.example.howstheweather.domain.usecases.GetLocationUseCase
import com.example.howstheweather.domain.usecases.GetWeatherForecastUseCase
import com.example.howstheweather.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentWeatherByLatLonUseCase: GetCurrentWeatherByLatLonUseCase,
    private val getLocationUseCase: GetLocationUseCase,
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase
): ViewModel() {

    private val _currentWeather = MutableLiveData<Response<CurrentWeather>>()
    val currentWeather: LiveData<Response<CurrentWeather>> = _currentWeather

    private val _forecastWeather = MutableLiveData<Response<List<ForecastWeather>>>()
    val forecastWeather: LiveData<Response<List<ForecastWeather>>> = _forecastWeather

    private val _cityName = MutableLiveData<Response<LocationData>>()
    val cityName: LiveData<Response<LocationData>> = _cityName

    private var isWeatherAvailable = false

    private fun getCurrentWeather(lat: String, lon: String) = viewModelScope.launch {
        _currentWeather.postValue(Response.Loading())
        isWeatherAvailable = true
        getCurrentWeatherByLatLonUseCase.invoke(lat, lon).also {
            _currentWeather.postValue(it)
        }
    }

    fun onLocationCallback(lat: String, lon: String) {
        if (!isWeatherAvailable) {
            getCityName(lat, lon)
        }
    }

    private fun getCityName(lat: String, lon: String) = viewModelScope.launch {
        _currentWeather.postValue(Response.Loading())
        getLocationUseCase.invoke(lat, lon).also {
            _cityName.postValue(it)
        }
    }

    fun onCityNameFetch(location: LocationData) {
        getCurrentWeather(location.lat, location.lon)
        getWeatherForecast(location.lat, location.lon)
    }

    private fun getWeatherForecast(lat: String, lon: String) = viewModelScope.launch {
        _forecastWeather.postValue(Response.Loading())
        getWeatherForecastUseCase.invoke(lat, lon).also {
            _forecastWeather.postValue(it)
        }
    }
}