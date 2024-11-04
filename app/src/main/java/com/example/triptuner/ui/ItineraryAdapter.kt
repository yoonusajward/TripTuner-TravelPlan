package com.example.triptuner.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.triptuner.databinding.ItemItineraryBinding
import com.example.triptuner.model.Itinerary

class ItineraryAdapter(
    private val onEditClick: (String) -> Unit,
    private val onDeleteClick: (String) -> Unit
) : ListAdapter<Itinerary, ItineraryAdapter.ItineraryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItineraryViewHolder {
        val binding = ItemItineraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItineraryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItineraryViewHolder, position: Int) {
        val itinerary = getItem(position)
        holder.bind(itinerary, onEditClick, onDeleteClick)
    }

    class ItineraryViewHolder(private val binding: ItemItineraryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(itinerary: Itinerary, onEditClick: (String) -> Unit, onDeleteClick: (String) -> Unit) {
            binding.tvDestination.text = "Destination: ${itinerary.destination}"
            binding.tvBudget.text = "Budget: ${itinerary.budget}"
            binding.tvCompanions.text = "Companions: ${itinerary.companions}"
            binding.tvSpecialNotes.text = "Special Notes: ${itinerary.specialNotes}"
            binding.tvGeneratedInformation.text = itinerary.generatedInformation

            // Show "Edited" label if the itinerary was edited
            binding.tvEdited.visibility = if (itinerary.edited) View.VISIBLE else View.GONE

            binding.btnEditItinerary.setOnClickListener {
                onEditClick(itinerary.id)
            }

            binding.btnDeleteItinerary.setOnClickListener {
                onDeleteClick(itinerary.id)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Itinerary>() {
        override fun areItemsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Itinerary, newItem: Itinerary): Boolean {
            return oldItem == newItem
        }
    }
}
