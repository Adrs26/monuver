package com.android.monuver.feature.account

import app.cash.turbine.test
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.GetAllAccountsUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAllAccountsUseCaseTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var getAllAccountsUseCase: GetAllAccountsUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        getAllAccountsUseCase = GetAllAccountsUseCase(accountRepository)
    }

    @Test
    fun `should emit list of user`() = runTest {
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

        whenever(accountRepository.getAllAccounts()).thenReturn(flowOf(accounts))

        val accountsFlow = getAllAccountsUseCase()

        accountsFlow.test {
            val accounts = awaitItem()
            assertEquals(2, accounts.size)
            assertEquals("BCA", accounts[0].name)
            assertEquals(1_000_000, accounts[0].balance)
            assertEquals("BRI", accounts[1].name)
            assertEquals(2_000_000, accounts[1].balance)
            awaitComplete()
        }

        verify(accountRepository).getAllAccounts()
    }
}