package com.android.monuver.feature.account

import app.cash.turbine.test
import com.android.monuver.core.domain.model.AccountState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.GetAccountByIdUseCase
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetAccountByIdUseCaseTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var getAccountByIdUseCase: GetAccountByIdUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        getAccountByIdUseCase = GetAccountByIdUseCase(accountRepository)
    }

    @Test
    fun `should emit user`() = runTest {
        val accountState = AccountState(
            id = 1,
            name = "BCA",
            type = AccountType.BANK,
            balance = 1_000_000,
            isActive = true
        )

        whenever(accountRepository.getAccountById(1)).thenReturn(flowOf(accountState))

        val accountFlow = getAccountByIdUseCase(1)

        accountFlow.test {
            val account = awaitItem()
            assertEquals(accountState, account)
            awaitComplete()
        }

        verify(accountRepository).getAccountById(1)
    }
}