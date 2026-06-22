package com.example.unitconverter.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CurrencyViewModel(private val repository: CurrencyRepository) : ViewModel() {
    var rates by mutableStateOf<List<CurrencyRate>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var isUsingOfflineRates by mutableStateOf(false)

    private var cachedFilteredList by mutableStateOf<List<CurrencyRate>>(emptyList())
    private var lastQuery by mutableStateOf("")

    init { fetchRates() }

    fun fetchRates() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            isUsingOfflineRates = false
            repository.getRates()
                .catch { e ->
                    if (rates.isNotEmpty() || cachedFilteredList.isNotEmpty()) {
                        isUsingOfflineRates = true
                        errorMessage = "Offline mode: Failed to sync latest rates."
                    } else {
                        errorMessage = "Network error: ${e.localizedMessage ?: "Failed to fetch rates."}"
                    }
                    isLoading = false
                }
                .collect {
                    rates = it
                    isUsingOfflineRates = false
                    errorMessage = null
                }
            isLoading = false
        }
    }

    fun getFilteredRates(query: String): List<CurrencyRate> {
        if (query == lastQuery && cachedFilteredList.isNotEmpty()) return cachedFilteredList

        val q = query.lowercase().trim()
        lastQuery = query

        cachedFilteredList = if (q.isEmpty()) {
            rates // The full 160+ list
        } else {
            rates.filter { it.code.contains(q, true) || it.fullName.contains(q, true) }
        }
        return cachedFilteredList
    }

    fun convert(amount: Double, from: String, to: String): Double {
        val fromRate = rates.find { it.code == from }?.rate ?: 1.0
        val toRate = rates.find { it.code == to }?.rate ?: 1.0
        return (amount / fromRate) * toRate
    }
}