package com.example.howstheweather.di

import com.example.howstheweather.data.remote.ApiKeyInterceptor
import com.example.howstheweather.data.remote.WeatherAPI
import com.example.howstheweather.data.repository.WeatherRepositoryImpl
import com.example.howstheweather.domain.repository.WeatherRepository
import com.example.howstheweather.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class HiltModule {

    @Singleton
    @Provides
    fun provideRetrofit(
        apiKeyInterceptor: ApiKeyInterceptor
    ): Retrofit {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val okhttpClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun provideWeatherAPI(retrofit: Retrofit): WeatherAPI =
        retrofit.create(WeatherAPI::class.java)

    @Singleton
    @Provides
    fun provideWeatherRepository(weatherAPI: WeatherAPI): WeatherRepository =
        WeatherRepositoryImpl(weatherAPI)
}