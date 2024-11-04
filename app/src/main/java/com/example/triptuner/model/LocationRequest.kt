package com.example.triptuner.model

data class LocationRequest(
    val inputs: String,  // This is the text input for the model
    val parameters: Parameters = Parameters()  // Optional parameters for the model
)

data class Parameters(
    val max_length: Int = 150,  // Maximum length of generated text
    val num_return_sequences: Int = 1  // Number of sequences to return
)
