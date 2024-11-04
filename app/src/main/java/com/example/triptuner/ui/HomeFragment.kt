package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set click listener for Create Itinerary button
        binding.btnCreateItinerary.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createItineraryFragment)
        }

        // Set click listener for View Itineraries button
        binding.btnViewItineraries.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_viewItineraryFragment)
        }

        // Set up Budget Planner button click listener
        binding.btnBudgetPlanner.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_navigation_budget_planner)
        }

        // Set click listener for User Profile button
        binding.btnUserProfile.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_userProfileFragment)
        }

        // Set click listener for Logout button
        binding.btnLogout.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_logoutFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
