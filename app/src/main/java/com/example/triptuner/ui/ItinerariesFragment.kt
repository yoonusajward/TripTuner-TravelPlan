package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.triptuner.databinding.FragmentItinerariesBinding
import com.example.triptuner.R

class ItinerariesFragment : Fragment() {

    private var _binding: FragmentItinerariesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItinerariesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for Create Itinerary card
        binding.cardCreateItinerary.setOnClickListener {
            findNavController().navigate(R.id.action_itinerariesFragment_to_createItineraryFragment)
        }

        // Set click listener for View Itineraries card
        binding.cardViewItineraries.setOnClickListener {
            findNavController().navigate(R.id.action_itinerariesFragment_to_viewItineraryFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
