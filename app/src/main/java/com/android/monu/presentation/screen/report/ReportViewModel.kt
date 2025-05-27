package com.android.monu.presentation.screen.report

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.monu.domain.usecase.GetAvailableTransactionYearsUseCase
import com.android.monu.domain.usecase.GetTransactionMonthlyAmountsUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReportViewModel(
    private val getAvailableTransactionYearsUseCase: GetAvailableTransactionYearsUseCase,
    private val getTransactionMonthlyAmountsUseCase: GetTransactionMonthlyAmountsUseCase
) : ViewModel() {

    private val _selectedYearFilter = MutableStateFlow<Int>(Calendar.getInstance().get(Calendar.YEAR))
    val selectedYearFilter = _selectedYearFilter.asStateFlow()

    private val _availableTransactionYears = MutableStateFlow<List<Int>>(emptyList())
    val availableTransactionYears = _availableTransactionYears.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val listTransactionMonthlyAmount = _selectedYearFilter.flatMapLatest { year ->
        getTransactionMonthlyAmountsUseCase.invoke(year)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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