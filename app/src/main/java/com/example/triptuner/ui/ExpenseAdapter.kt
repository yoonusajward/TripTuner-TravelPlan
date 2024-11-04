package com.example.triptuner.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.triptuner.R
import com.example.triptuner.databinding.ItemExpenseBinding
import com.example.triptuner.model.Expense
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ExpenseAdapter :
    ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(private val binding: ItemExpenseBinding) : RecyclerView.ViewHolder(binding.root) {
        private val db = Firebase.firestore

        fun bind(expense: Expense) {
            binding.tvExpenseName.text = expense.name
            binding.tvExpenseAmount.text = binding.root.context.getString(R.string.expense_amount, expense.amount)

            // Handle delete action
            binding.ivDeleteExpense.setOnClickListener {
                // Delete the expense from Firestore
                expense.id?.let { expenseId ->
                    db.collection("expenses").document(expenseId)
                        .delete()
                        .addOnSuccessListener {
                            Toast.makeText(binding.root.context, "Expense deleted successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(binding.root.context, "Failed to delete expense", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
