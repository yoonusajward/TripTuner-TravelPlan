package com.example.triptuner.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Hugging Face API base URL
    private const val BASE_URL = "https://api-inference.huggingface.co/models/yoonusajward/ttmodel/"

    // Logging interceptor for debugging API calls
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient with Authorization header
    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer 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")
                .build()
            chain.proceed(request)
        }
        .build()

    // Retrofit instance with Gson converter
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Service instance for making API calls
    val api: TripTunerApiService by lazy {
        retrofit.create(TripTunerApiService::class.java)
    }
}
