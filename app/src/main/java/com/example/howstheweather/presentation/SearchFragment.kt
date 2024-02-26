package com.example.howstheweather.presentation

import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.howstheweather.R
import com.example.howstheweather.databinding.FragmentSearchBinding
import com.example.howstheweather.domain.model.CurrentWeather
import com.example.howstheweather.utils.Response
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel: SearchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initObserver()
        initListener()
    }

    private fun initObserver() {
        searchViewModel.currentWeather.observe(viewLifecycleOwner) {
            handleCurrentWeatherResponse(it)
        }
    }

    private fun handleCurrentWeatherResponse(response: Response<CurrentWeather>) {
        when (response) {
            is Response.Error -> {
                Toast.makeText(context, response.error ?: "Something went wrong", Toast.LENGTH_SHORT).show()
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
            tvHeading.text = "Current weather details for " + binding.etSearch.text.trim().toString() + ": "
            Glide.with(requireContext())
                .load(weather.weatherIconUrl)
                .into(ivWeather)
            tvWeatherDescription.hint = weather.weatherDescription
            tvTemp.text = "Current temperature is " + weather.temp + "°C"
            tvTempFeelsLike.text = "Temperature feels like " + weather.tempFeelsLike + "°C"
            tvTempMax.text = "Today's maximum temperature is " + weather.maxTemp + "°C"
            tvTempMin.text = "Today's minimum temperature is " + weather.minTemp + "°C"
        }
    }

    private fun initListener() {
        binding.ivSearch.setOnClickListener {
            binding.etSearch.text.trim().toString().let {
                if (it.isEmpty()) {
                    Toast.makeText(context, "Please enter a City", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                searchViewModel.onSearchIconClick(it)
            }
        }
    }

    private fun initUI() {
        with(binding) {
            groupLoading.visibility = View.GONE
            groupWeatherData.visibility = View.GONE
            etSearch.requestFocus()
        }
        showKeyboard()

    }

    private fun showKeyboard() {
        val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.etSearch, InputMethodManager.SHOW_IMPLICIT)
    }
}