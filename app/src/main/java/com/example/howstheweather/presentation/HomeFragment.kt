package com.example.howstheweather.presentation

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.howstheweather.R
import com.example.howstheweather.databinding.FragmentHomeBinding
import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.domain.model.ForecastWeather
import com.example.howstheweather.domain.model.LocationData
import com.example.howstheweather.utils.Response
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val permissionContract = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        getCurrentLatLong()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getCurrentLatLong()
        initObservers()
        initListener()
    }

    private fun initListener() {
        binding.viewSearch.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                val searchFragment = SearchFragment()
                add(R.id.fragment_container_view, searchFragment)
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun getCurrentLatLong() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestNewLocationData()
    }

    private fun initObservers() {
        homeViewModel.currentWeather.observe(viewLifecycleOwner) {
            handleCurrentWeatherResponse(it)
        }
        homeViewModel.cityName.observe(viewLifecycleOwner) {
            handleCityNameResponse(it)
        }
        homeViewModel.forecastWeather.observe(viewLifecycleOwner) {
            handleForecastWeatherResponse(it)
        }
    }

    private fun handleForecastWeatherResponse(response: Response<List<ForecastWeather>>) {
        when (response) {
            is Response.Error -> {
                Toast.makeText(context, "Error while getting forecast", Toast.LENGTH_SHORT).show()
            }
            is Response.Loading -> {}
            is Response.Success -> {
                if (response.data == null) {
                    Toast.makeText(context, "Empty response from server", Toast.LENGTH_SHORT).show()
                    return
                }
                setForecastData(response.data)
            }
        }
    }

    private fun setForecastData(list: List<ForecastWeather>) {
        binding.rvForecast.adapter = WeatherForecastAdapter(requireContext(), list) {
            showForecastDialog(it)
        }
        binding.rvForecast.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun handleCityNameResponse(response: Response<LocationData>) {
        when (response) {
            is Response.Error -> {
                Toast.makeText(context, "Error while getting city", Toast.LENGTH_SHORT).show()
            }
            is Response.Loading -> {}
            is Response.Success -> {
                if (response.data == null) {
                    Toast.makeText(context, "Empty response from server", Toast.LENGTH_SHORT).show()
                    return
                }
                homeViewModel.onCityNameFetch(response.data)
            }
        }
    }

    private fun handleCurrentWeatherResponse(response: Response<CurrentWeather>) {
        when (response) {
            is Response.Error -> {
                binding.groupLoading.visibility = View.GONE
                binding.groupWeatherData.visibility = View.VISIBLE
            }
            is Response.Loading -> {
                binding.groupLoading.visibility = View.VISIBLE
                binding.groupWeatherData.visibility = View.INVISIBLE
            }
            is Response.Success -> {
                if (response.data == null) {
                    Toast.makeText(context, "Empty response from server", Toast.LENGTH_SHORT).show()
                    return
                }
                binding.groupLoading.visibility = View.GONE
                binding.groupWeatherData.visibility = View.VISIBLE
                setWeatherData(response.data)
            }
        }
    }

    private fun setWeatherData(weather: CurrentWeather) {
        with(binding) {
            tvCity.text = homeViewModel.cityName.value?.data?.name + ", " + homeViewModel.cityName.value?.data?.country
            Glide.with(requireContext())
                .load(weather.weatherIconUrl)
                .into(ivWeather)
            tvWeatherDescription.hint = weather.weatherDescription
            tvTemp.text = "Temp: " + weather.temp + "°C"
            tvTempFeelsLike.text = "Feels like: " + weather.tempFeelsLike + "°C"
            tvTempMax.text = "Max Temp: " + weather.maxTemp + "°C"
            tvTempMin.text = "Min Temp: " + weather.minTemp + "°C"
        }
    }

    private fun checkPermissions(): Boolean {
        return (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun requestPermissions() {
        permissionContract.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        if (!checkPermissions()) {
            requestPermissions()
            return
        }
        if (!isLocationEnabled()) {
            Toast.makeText(context, "Please turn on location..", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
            return
        }
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0)
            .setIntervalMillis(0)
            .setMinUpdateIntervalMillis(0)
            .setMaxUpdates(Int.MAX_VALUE)
            .build()

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback,
            Looper.myLooper()
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val lastLocation: Location = locationResult.lastLocation!!
            homeViewModel.onLocationCallback(lastLocation.latitude.toString(), lastLocation.longitude.toString())
        }
    }

    private fun showForecastDialog(data: ForecastWeather) {
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_forecast_weather)

        val tvDate: TextView = dialog.findViewById(R.id.tv_city)
        tvDate.text = data.date
        val ivWeather: ImageView = dialog.findViewById(R.id.iv_weather)
        Glide.with(requireContext())
            .load(data.weatherIconUrl)
            .into(ivWeather)
        val tvDesc: TextView = dialog.findViewById(R.id.tv_weather_description)
        tvDesc.text = data.weatherDescription
        val tvTemp: TextView = dialog.findViewById(R.id.tv_temp)
        tvTemp.text = "Temp: " + data.temp + "°C"
        val tvFeelsLike: TextView = dialog.findViewById(R.id.tv_temp_feels_like)
        tvFeelsLike.text = "Feels like: " + data.tempFeelsLike + "°C"
        val tvTempMax: TextView = dialog.findViewById(R.id.tv_temp_max)
        tvTempMax.text = "Max Temp: " + data.maxTemp + "°C"
        val tvTempMin: TextView = dialog.findViewById(R.id.tv_temp_min)
        tvTempMin.text = "Min Temp: " + data.minTemp + "°C"

        val window = dialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)

        dialog.show()
    }

}