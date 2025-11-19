package com.android.monuver.feature.account

import app.cash.turbine.test
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAccountByIdUseCaseTest {

    private lateinit var repository: AccountRepository
    private lateinit var getAccountByIdUseCase: GetAccountByIdUseCase

    @Before
    fun setup() {
        repository = mock(AccountRepository::class.java)
        getAccountByIdUseCase = GetAccountByIdUseCase(repository)
    }

    @Test
    fun `should emit user`() = runTest {
        val expected = AccountState(
            id = 1,
            name = "BCA",
            type = AccountType.BANK,
            balance = 1_000_000,
            isActive = true
        )

        whenever(repository.getAccountById(1)).thenReturn(flowOf(expected))

        val flow = getAccountByIdUseCase(1)

        flow.test {
            val result = awaitItem()
            assertThat(result).isEqualTo(expected)
            awaitComplete()
        }

        verify(repository).getAccountById(1)
    }
}