package com.example.howstheweather.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.usecases.GetCurrentWeatherByCityUseCase
import com.example.howstheweather.utils.Response
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCurrentWeatherByCityUseCase: GetCurrentWeatherByCityUseCase
): ViewModel() {

    private val _currentWeather = MutableLiveData<Response<CurrentWeather>>()
    val currentWeather: LiveData<Response<CurrentWeather>> = _currentWeather

    private fun getCurrentWeather(city: String) = viewModelScope.launch {
        _currentWeather.postValue(Response.Loading())
        getCurrentWeatherByCityUseCase.invoke(city).also {
            _currentWeather.postValue(it)
        }
    }

    fun onSearchIconClick(city: String) {
        getCurrentWeather(city)
    }
}