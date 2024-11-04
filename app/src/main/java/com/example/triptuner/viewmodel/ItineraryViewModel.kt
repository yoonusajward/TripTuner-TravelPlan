package com.example.triptuner.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.triptuner.model.Itinerary
import com.example.triptuner.model.LocationRequest
import com.example.triptuner.model.LocationResponse
import com.example.triptuner.network.RetrofitClient
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log

class ItineraryViewModel : ViewModel() {

    private val db: FirebaseFirestore = Firebase.firestore

    private val _itineraries = MutableLiveData<List<Itinerary>>()
    val itineraries: LiveData<List<Itinerary>> get() = _itineraries

    private val _itinerary = MutableLiveData<Itinerary?>()
    val itinerary: LiveData<Itinerary?> get() = _itinerary

    private val _generatedItinerary = MutableLiveData<String>()
    val generatedItinerary: LiveData<String> get() = _generatedItinerary

    private val _errorMessage = MutableLiveData<String>() // Added errorMessage LiveData
    val errorMessage: LiveData<String> get() = _errorMessage

    private val apiService = RetrofitClient.api
    private val tag = "ItineraryViewModel"

    // Fetch all itineraries from Firestore
    fun fetchAllItineraries() {
        db.collection("itineraries")
            .get()
            .addOnSuccessListener { result ->
                val itineraryList = result.documents.mapNotNull { document ->
                    document.toObject(Itinerary::class.java)?.copy(id = document.id)
                }
                _itineraries.value = itineraryList
                Log.d(tag, "Fetched all itineraries successfully.")
            }
            .addOnFailureListener { e ->
                _errorMessage.value = "Error fetching itineraries: ${e.message}" // Set error message
                Log.e(tag, "Error fetching itineraries: ${e.message}")
            }
    }

    // Fetch a specific itinerary by its ID
    fun fetchItinerary(itineraryId: String) {
        if (itineraryId.isNotEmpty()) {
            db.collection("itineraries").document(itineraryId)
                .get()
                .addOnSuccessListener { document ->
                    _itinerary.value = document.toObject(Itinerary::class.java)?.copy(id = itineraryId)
                    Log.d(tag, "Fetched itinerary successfully: $itineraryId")
                }
                .addOnFailureListener { e ->
                    _itinerary.value = null
                    _errorMessage.value = "Error fetching itinerary: ${e.message}" // Set error message
                    Log.e(tag, "Error fetching itinerary: ${e.message}")
                }
        }
    }

    // Update an existing itinerary
    fun updateItinerary(itineraryId: String, updatedItinerary: Itinerary) {
        if (itineraryId.isNotEmpty()) {
            db.collection("itineraries").document(itineraryId)
                .set(updatedItinerary)
                .addOnSuccessListener {
                    fetchAllItineraries()
                    Log.d(tag, "Updated itinerary successfully: $itineraryId")
                }
                .addOnFailureListener { e ->
                    _errorMessage.value = "Error updating itinerary: ${e.message}" // Set error message
                    Log.e(tag, "Error updating itinerary: ${e.message}")
                }
        }
    }

    // Delete an itinerary by its ID
    fun deleteItinerary(itineraryId: String) {
        if (itineraryId.isNotEmpty()) {
            db.collection("itineraries").document(itineraryId)
                .delete()
                .addOnSuccessListener {
                    fetchAllItineraries()
                    Log.d(tag, "Deleted itinerary successfully: $itineraryId")
                }
                .addOnFailureListener { e ->
                    _errorMessage.value = "Error deleting itinerary: ${e.message}" // Set error message
                    Log.e(tag, "Error deleting itinerary: ${e.message}")
                }
        }
    }

    // Fetch itinerary from the Hugging Face model
    fun fetchItineraryFromModel(prompt: String) {
        val requestBody = LocationRequest(inputs = prompt)
        apiService.generateItinerary(requestBody).enqueue(object : Callback<List<LocationResponse>> {
            override fun onResponse(
                call: Call<List<LocationResponse>>,
                response: Response<List<LocationResponse>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    _generatedItinerary.value = response.body()!![0].generated_text
                    Log.d(tag, "Generated itinerary successfully from model.")
                } else {
                    _generatedItinerary.value = "Error: ${response.errorBody()?.string()}"
                    _errorMessage.value = "Error generating itinerary from model: ${response.errorBody()?.string()}"
                    Log.e(tag, "Error generating itinerary from model: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                _generatedItinerary.value = "Failure: ${t.message}"
                _errorMessage.value = "Model API call failed: ${t.message}"
                Log.e(tag, "Model API call failed: ${t.message}")
            }
        })
    }
}
