package com.android.monuver.feature.account

import app.cash.turbine.test
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.GetTotalAccountBalanceUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetTotalAccountBalanceUseCaseTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        getTotalAccountBalanceUseCase = GetTotalAccountBalanceUseCase(accountRepository)
    }

    @Test
    fun `should emit total account balance`() = runTest {
        val accounts = listOf(
            AccountState(
                id = 1,
                name = "BCA",
                type = AccountType.BANK,
                balance = 1_000_000,
                isActive = true
            ),
            AccountState(
                id = 2,
                name = "BRI",
                type = AccountType.BANK,
                balance = 2_000_000,
                isActive = true
            )
        )

        whenever(accountRepository.getTotalAccountBalance())
            .thenReturn(flowOf(accounts.sumOf { it.balance }))

        val totalBalanceFlow = getTotalAccountBalanceUseCase()

        totalBalanceFlow.test {
            val totalBalance = awaitItem()
            assertEquals(3_000_000L, totalBalance)
            awaitComplete()
        }

        verify(accountRepository).getTotalAccountBalance()
    }
}