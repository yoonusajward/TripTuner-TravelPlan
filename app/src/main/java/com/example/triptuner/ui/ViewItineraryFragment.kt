package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentViewItineraryBinding
import com.example.triptuner.viewmodel.ItineraryViewModel

class ViewItineraryFragment : Fragment() {

    private var _binding: FragmentViewItineraryBinding? = null
    private val binding get() = _binding!!
    private val itineraryViewModel: ItineraryViewModel by viewModels()
    private lateinit var adapter: ItineraryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentViewItineraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeItineraries()
        itineraryViewModel.fetchAllItineraries()

        binding.btnCreateItinerary.setOnClickListener {
            findNavController().navigate(R.id.action_viewItineraryFragment_to_createItineraryFragment)
        }

        // Handle the refresh action to fetch the latest itineraries
        binding.swipeRefreshLayout.setOnRefreshListener {
            itineraryViewModel.fetchAllItineraries()
        }
    }

    private fun setupRecyclerView() {
        adapter = ItineraryAdapter(
            onEditClick = { itineraryId ->
                val action = ViewItineraryFragmentDirections.actionViewItineraryFragmentToEditItineraryFragment(itineraryId)
                findNavController().navigate(action)
            },
            onDeleteClick = { itineraryId ->
                itineraryViewModel.deleteItinerary(itineraryId)
            }
        )
        binding.recyclerViewItineraries.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewItineraries.adapter = adapter
    }

    private fun observeItineraries() {
        itineraryViewModel.itineraries.observe(viewLifecycleOwner) { itineraries ->
            adapter.submitList(itineraries)
            binding.swipeRefreshLayout.isRefreshing = false // Stop the refreshing indicator
        }

        itineraryViewModel.errorMessage.observe(viewLifecycleOwner) { message: String? -> // Specify type explicitly
            // Display an error message or take necessary actions on error
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
