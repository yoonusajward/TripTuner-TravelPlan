package com.example.triptuner.network

import com.example.triptuner.model.LocationRequest
import com.example.triptuner.model.LocationResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TripTunerApiService {
    @POST(".") // Endpoint to call the model
    fun generateItinerary(@Body requestBody: LocationRequest): Call<List<LocationResponse>> // List is used as Hugging Face may return multiple sequences
}
