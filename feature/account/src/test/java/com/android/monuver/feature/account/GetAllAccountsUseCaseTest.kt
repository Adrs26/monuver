package com.android.monuver.feature.account

import app.cash.turbine.test
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.GetAllAccountsUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAllAccountsUseCaseTest {

    private lateinit var repository: AccountRepository
    private lateinit var getAllAccountsUseCase: GetAllAccountsUseCase

    @Before
    fun setup() {
        repository = mock(AccountRepository::class.java)
        getAllAccountsUseCase = GetAllAccountsUseCase(repository)
    }

    @Test
    fun `should emit list of user`() = runTest {
        val expected = listOf(
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

        whenever(repository.getAllAccounts()).thenReturn(flowOf(expected))

        val flow = getAllAccountsUseCase()

        flow.test {
            val result = awaitItem()
            assertThat(result).containsExactlyElementsIn(expected)
            awaitComplete()
        }

        verify(repository).getAllAccounts()
    }
}