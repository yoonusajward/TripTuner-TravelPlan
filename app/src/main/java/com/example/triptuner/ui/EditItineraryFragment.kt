package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentEditItineraryBinding
import com.example.triptuner.model.Itinerary
import com.example.triptuner.viewmodel.ItineraryViewModel

class EditItineraryFragment : Fragment() {

    private var _binding: FragmentEditItineraryBinding? = null
    private val binding get() = _binding!!
    private val itineraryViewModel: ItineraryViewModel by viewModels()
    private val args: EditItineraryFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itineraryId = args.itineraryId
        itineraryViewModel.fetchItinerary(itineraryId)

        itineraryViewModel.itinerary.observe(viewLifecycleOwner) { itinerary ->
            if (itinerary != null) {
                setupFields(itinerary)
            } else {
                Toast.makeText(requireContext(), "Failed to load itinerary.", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp() // Navigate back if data is null
            }
        }

        setupCompanionsSpinner()

        binding.btnSaveItinerary.setOnClickListener {
            saveUpdatedItinerary(itineraryId)
        }
    }

    private fun setupFields(itinerary: Itinerary) {
        binding.etDestinationEdit.setText(itinerary.destination)
        binding.etBudgetEdit.setText(itinerary.budget) // Updated to use TextInputEditText
        binding.spCompanionsEdit.setSelection(getCompanionPosition(itinerary.companions))
        binding.etSpecialNotesEdit.setText(itinerary.specialNotes)
        binding.etGeneratedInformationEdit.setText(itinerary.generatedInformation)
    }

    private fun saveUpdatedItinerary(itineraryId: String) {
        val destination = binding.etDestinationEdit.text.toString().trim()
        val budget = binding.etBudgetEdit.text.toString().trim()  // Updated to fetch from TextInputEditText
        val companions = binding.spCompanionsEdit.selectedItem?.toString()
        val specialNotes = binding.etSpecialNotesEdit.text.toString().trim()
        val generatedInformation = binding.etGeneratedInformationEdit.text.toString().trim()

        // Check for null values and show a message if needed
        if (destination.isEmpty() || budget.isEmpty() || companions == null) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val updatedItinerary = Itinerary(
            id = itineraryId,
            destination = destination,
            budget = budget,
            companions = companions,
            specialNotes = specialNotes,
            generatedInformation = generatedInformation,
            edited = true
        )

        itineraryViewModel.updateItinerary(itineraryId, updatedItinerary)

        Toast.makeText(requireContext(), "Itinerary updated successfully!", Toast.LENGTH_SHORT).show()
        findNavController().navigateUp()
    }

    private fun getCompanionPosition(companions: String): Int {
        val companionsList = resources.getStringArray(R.array.companion_options)
        return companionsList.indexOf(companions).takeIf { it >= 0 } ?: 0
    }

    private fun setupCompanionsSpinner() {
        val companions = resources.getStringArray(R.array.companion_options)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, companions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCompanionsEdit.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
