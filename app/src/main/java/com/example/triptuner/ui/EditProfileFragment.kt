package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSaveProfile.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter your name.", Toast.LENGTH_SHORT).show()
            } else {
                // Handle save action for the name
                Toast.makeText(requireContext(), "Profile saved successfully!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.tvChangePassword.setOnClickListener {
            // Navigate to the Change Password screen
            findNavController().navigate(R.id.action_editProfileFragment_to_changePasswordFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
