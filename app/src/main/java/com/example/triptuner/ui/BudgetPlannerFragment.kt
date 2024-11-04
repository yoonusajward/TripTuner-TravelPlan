package com.example.triptuner.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.triptuner.R
import com.example.triptuner.databinding.FragmentBudgetPlannerBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.triptuner.model.Expense

class BudgetPlannerFragment : Fragment() {

    private var _binding: FragmentBudgetPlannerBinding? = null
    private val binding get() = _binding!!
    private val db = Firebase.firestore
    private lateinit var adapter: ExpenseAdapter
    private var totalExpenses: Double = 0.0
    private var userBudget: Double = 0.0  // Variable to store the user's budget

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetPlannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        loadExpenses()
        loadBudget()  // Load the budget on view creation

        binding.btnAddExpense.setOnClickListener {
            addExpense()
        }

        binding.btnSetBudget.setOnClickListener {
            setBudget()
        }
    }

    private fun setupRecyclerView() {
        adapter = ExpenseAdapter()
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    // Load the expenses from Firestore
    private fun loadExpenses() {
        db.collection("expenses")
            .get()
            .addOnSuccessListener { result ->
                val expenses = result.documents.mapNotNull { document ->
                    document.toObject(Expense::class.java)?.apply { id = document.id }
                }
                adapter.submitList(expenses)
                calculateTotalExpenses(expenses)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), R.string.failed_to_load_expenses, Toast.LENGTH_SHORT).show()
            }
    }

    // Load the budget from Firestore
    private fun loadBudget() {
        db.collection("budget").document("user_budget")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val budget = document.getDouble("budget") ?: 0.0
                    userBudget = budget
                    binding.etBudget.setText(budget.toString())  // Set the budget in the input field
                    checkBudgetAgainstTotal()  // Check if the current expenses exceed the budget
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), R.string.failed_to_load_expenses, Toast.LENGTH_SHORT).show()
            }
    }

    // Calculate the total expenses
    private fun calculateTotalExpenses(expenses: List<Expense>) {
        totalExpenses = expenses.sumOf { it.amount }
        binding.tvTotalExpenses.text = getString(R.string.total_expenses, totalExpenses)
    }

    // Add a new expense
    private fun addExpense() {
        val name = binding.etExpenseName.text.toString().trim()
        val amountText = binding.etExpenseAmount.text.toString().trim()

        if (name.isEmpty() || amountText.isEmpty()) {
            Toast.makeText(requireContext(), R.string.fill_all_fields, Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            Toast.makeText(requireContext(), R.string.invalid_amount, Toast.LENGTH_SHORT).show()
            return
        }

        val expense = Expense(name = name, amount = amount, currency = "LKR")
        db.collection("expenses")
            .add(expense)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), R.string.expense_added_successfully, Toast.LENGTH_SHORT).show()
                loadExpenses()
                clearInputFields()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), R.string.failed_to_add_expense, Toast.LENGTH_SHORT).show()
            }
    }

    // Save the user's budget to Firestore
    private fun setBudget() {
        val budgetText = binding.etBudget.text.toString().trim()

        if (budgetText.isEmpty()) {
            Toast.makeText(requireContext(), R.string.enter_budget, Toast.LENGTH_SHORT).show()
            return
        }

        val budget = budgetText.toDoubleOrNull()
        if (budget != null && budget > 0) {
            // Save the budget to Firestore
            val budgetData = hashMapOf("budget" to budget)
            db.collection("budget").document("user_budget")
                .set(budgetData)
                .addOnSuccessListener {
                    userBudget = budget
                    checkBudgetAgainstTotal()  // Check if the current expenses exceed the budget
                    Toast.makeText(requireContext(), "Budget set successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to set budget", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), R.string.invalid_budget, Toast.LENGTH_SHORT).show()
        }
    }

    // Check if the total expenses exceed the user's set budget
    private fun checkBudgetAgainstTotal() {
        if (totalExpenses > userBudget) {
            binding.tvBudgetWarning.text = getString(R.string.budget_exceeded, totalExpenses, userBudget)
            binding.tvBudgetWarning.visibility = View.VISIBLE
        } else {
            binding.tvBudgetWarning.visibility = View.GONE
            Toast.makeText(requireContext(), "Your expenses are within the budget.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun clearInputFields() {
        binding.etExpenseName.text.clear()
        binding.etExpenseAmount.text.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
