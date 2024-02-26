package com.example.howstheweather.data.remote

import com.example.howstheweather.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class ApiKeyInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestUrl = chain.request().url.toString()
        val request = chain.request().newBuilder()
        request.url(requestUrl + "&appId=" + Constants.API_KEY)
        return chain.proceed(request.build())
    }
}