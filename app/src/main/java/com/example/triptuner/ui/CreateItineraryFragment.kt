package com.example.triptuner.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentCreateItineraryBinding
import com.example.triptuner.viewmodel.ItineraryViewModel
import com.google.firebase.firestore.FirebaseFirestore

class CreateItineraryFragment : Fragment() {

    private var _binding: FragmentCreateItineraryBinding? = null
    private val binding get() = _binding!!
    private val tag = "CreateItineraryFragment"
    private val itineraryViewModel: ItineraryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCompanionsSpinner()

        binding.btnGenerateItinerary.setOnClickListener {
            generateItinerary()
        }

        observeGeneratedItinerary() // Observe generated itinerary LiveData
    }

    private fun setupCompanionsSpinner() {
        val companions = resources.getStringArray(R.array.companion_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, companions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCompanions.adapter = adapter
    }

    private fun generateItinerary() {
        val destination = binding.etDestination.text.toString().trim()

        if (destination.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Fetch itinerary from the model using the ViewModel
        itineraryViewModel.fetchItineraryFromModel(destination)
    }

    private fun observeGeneratedItinerary() {
        itineraryViewModel.generatedItinerary.observe(viewLifecycleOwner) { generatedInfo ->
            // Handle the generated itinerary, e.g., save it to Firestore or display it
            if (generatedInfo.startsWith("Error:") || generatedInfo.startsWith("Failure:")) {
                Toast.makeText(requireContext(), generatedInfo, Toast.LENGTH_SHORT).show()
            } else {
                saveItineraryToFirestore(binding.etDestination.text.toString(), generatedInfo)
            }
        }

        itineraryViewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (!error.isNullOrEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveItineraryToFirestore(destination: String, generatedInfo: String) {
        val budget = binding.etBudget.text.toString().trim()
        val companions = binding.spCompanions.selectedItem?.toString() ?: ""
        val specialNotes = binding.etSpecialNotes.text.toString().trim()

        val itineraryData = hashMapOf(
            "destination" to destination,
            "budget" to budget,
            "companions" to companions,
            "specialNotes" to specialNotes,
            "generatedInformation" to generatedInfo
        )

        // Save to Firestore collection "itineraries"
        FirebaseFirestore.getInstance().collection("itineraries")
            .add(itineraryData)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Itinerary saved successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_createItineraryFragment_to_viewItineraryFragment)
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to save itinerary: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e(tag, "Error saving itinerary: ${e.message}")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
