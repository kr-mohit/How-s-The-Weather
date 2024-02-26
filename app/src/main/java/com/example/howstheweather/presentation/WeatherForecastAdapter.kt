package com.example.howstheweather.presentation

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howstheweather.databinding.ItemWeatherForecastBinding
import com.example.howstheweather.domain.model.ForecastWeather

class WeatherForecastAdapter(
    private val context: Context,
    private val list: List<ForecastWeather>,
    private val onItemClick: (ForecastWeather)->Unit
): RecyclerView.Adapter<WeatherForecastAdapter.WeatherForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherForecastViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherForecastViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: WeatherForecastViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }

    inner class WeatherForecastViewHolder(private val binding: ItemWeatherForecastBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ForecastWeather) {
            binding.apply {
                tvDate.text = item.date
                Glide.with(context)
                    .load(item.weatherIconUrl)
                    .into(ivWeather)
                tvWeatherName.text = item.weatherTitle
                tvTempMaxMin.text = item.maxTemp + "℃/" + item.minTemp + "℃"
                clItemWeatherForecast.setOnClickListener {
                    onItemClick(item)
                }
            }
        }

    }
}