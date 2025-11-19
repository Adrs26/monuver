package com.android.monuver.feature.account

import app.cash.turbine.test
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.GetTotalAccountBalanceUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetTotalAccountBalanceUseCaseTest {

    private lateinit var repository: AccountRepository
    private lateinit var getTotalAccountBalanceUseCase: GetTotalAccountBalanceUseCase

    @Before
    fun setup() {
        repository = mock(AccountRepository::class.java)
        getTotalAccountBalanceUseCase = GetTotalAccountBalanceUseCase(repository)
    }

    @Test
    fun `should emit total account balance`() = runTest {
        whenever(repository.getTotalAccountBalance()).thenReturn(flowOf(3_000_000L))

        val flow = getTotalAccountBalanceUseCase()

        flow.test {
            val totalBalance = awaitItem()
            assertThat(totalBalance).isEqualTo(3_000_000L)
            awaitComplete()
        }

        verify(repository).getTotalAccountBalance()
    }
}