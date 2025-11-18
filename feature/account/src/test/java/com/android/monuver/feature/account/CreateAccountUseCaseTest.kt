package com.android.monuver.feature.account

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.account.domain.model.AddAccountState
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.CreateAccountUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.check
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever

class CreateAccountUseCaseTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var createAccountUseCase: CreateAccountUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        createAccountUseCase = CreateAccountUseCase(accountRepository)
    }

    @Test
    fun `should return error when account name is empty`() = runTest {
        val addAccountState = AddAccountState(
            name = "",
            type = AccountType.BANK,
            balance = 1_000_000
        )

        val result = createAccountUseCase(addAccountState)

        assert(result is DatabaseResultState.EmptyAccountName)
        verifyNoInteractions(accountRepository)
    }

    @Test
    fun `should return error when account type is empty`() = runTest {
        val addAccountState = AddAccountState(
            name = "BCA",
            type = 0,
            balance = 1_000_000
        )

        val result = createAccountUseCase(addAccountState)

        assert(result is DatabaseResultState.EmptyAccountType)
        verifyNoInteractions(accountRepository)
    }

    @Test
    fun `should return error when account balance is empty`() = runTest {
        val addAccountState = AddAccountState(
            name = "BCA",
            type = AccountType.BANK,
            balance = 0L
        )

        val result = createAccountUseCase(addAccountState)

        assert(result is DatabaseResultState.EmptyAccountBalance)
        verifyNoInteractions(accountRepository)
    }

    @Test
    fun `should return success when account is created`() = runTest {
        val addAccountState = AddAccountState(
            name = "BCA",
            type = AccountType.BANK,
            balance = 1_000_000
        )

        whenever(accountRepository.createAccount(any(), any())).thenReturn(Unit)

        val result = createAccountUseCase(addAccountState)

        assert(result is DatabaseResultState.CreateAccountSuccess)

        verify(accountRepository).createAccount(
            accountState = check {
                assertEquals("BCA", it.name)
                assertEquals(AccountType.BANK, it.type)
                assertEquals(1_000_000, it.balance)
                assertTrue(it.isActive)
            },
            transactionState = check {
                assertEquals("Penambahan Akun", it.title)
                assertEquals(TransactionType.INCOME, it.type)
                assertEquals(1_000_000, it.amount)
                assertEquals("BCA", it.sourceName)
                assertTrue(it.isLocked)
            }
        )
    }
}