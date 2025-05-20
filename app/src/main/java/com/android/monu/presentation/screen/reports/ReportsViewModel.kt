package com.android.monu.presentation.screen.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReportsViewModel(
    private val getAvailableTransactionYearsUseCase: GetAvailableTransactionYearsUseCase
) : ViewModel() {

    private val _availableTransactionYears = MutableStateFlow<List<Int>>(emptyList())
    val availableTransactionYears = _availableTransactionYears.asStateFlow()

    private val _selectedYearFilter = MutableStateFlow<Int>(2025)
    val selectedYearFilter = _selectedYearFilter.asStateFlow()

    fun loadAvailableTransactionYears() {
        viewModelScope.launch {
            getAvailableTransactionYearsUseCase.invoke().collect { availableYears ->
                _availableTransactionYears.value = availableYears
            }
        }
    }

    fun selectYear(year: Int) {
        _selectedYearFilter.value = year
    }
}