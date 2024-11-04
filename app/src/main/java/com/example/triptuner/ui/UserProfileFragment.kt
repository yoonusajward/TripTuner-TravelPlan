package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            loadUserProfile(currentUser)
        } else {
            Toast.makeText(requireContext(), "User not logged in.", Toast.LENGTH_SHORT).show()
        }

        binding.btnLogout.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_logoutFragment)
        }

        binding.btnViewItineraries.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_viewItineraryFragment)
        }

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_editProfileFragment)
        }

        binding.btnChangePassword.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_changePasswordFragment)
        }
    }

    private fun loadUserProfile(user: FirebaseUser) {
        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { document ->
                if (isAdded && _binding != null) { // Check if fragment is added and binding is not null
                    if (document != null && document.exists()) {
                        val name = document.getString("name") ?: "User"
                        val email = document.getString("email") ?: "user@example.com"

                        // Ensure binding is not null before accessing it
                        binding.tvGreeting.text = getString(R.string.greeting_text, name)
                        binding.tvEmailInfo.text = email
                    } else {
                        Toast.makeText(requireContext(), "User profile not found.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { exception ->
                if (isAdded && _binding != null) {
                    Toast.makeText(requireContext(), "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
