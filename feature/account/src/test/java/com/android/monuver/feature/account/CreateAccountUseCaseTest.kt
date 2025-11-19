package com.android.monuver.feature.account

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.core.domain.util.TransactionType
import com.android.monuver.feature.account.domain.model.AddAccountState
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.CreateAccountUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
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

    private lateinit var repository: AccountRepository
    private lateinit var createAccountUseCase: CreateAccountUseCase

    @Before
    fun setup() {
        repository = mock(AccountRepository::class.java)
        createAccountUseCase = CreateAccountUseCase(repository)
    }

    @Test
    fun `should return error when account name is empty`() = runTest {
        val addAccountState = AddAccountState(
            name = "",
            type = AccountType.BANK,
            balance = 1_000_000
        )

        val result = createAccountUseCase(addAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.EmptyAccountName)
        verifyNoInteractions(repository)
    }

    @Test
    fun `should return error when account type is empty`() = runTest {
        val addAccountState = AddAccountState(
            name = "BCA",
            type = 0,
            balance = 1_000_000
        )

        val result = createAccountUseCase(addAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.EmptyAccountType)
        verifyNoInteractions(repository)
    }

    @Test
    fun `should return error when account balance is empty`() = runTest {
        val addAccountState = AddAccountState(
            name = "BCA",
            type = AccountType.BANK,
            balance = 0L
        )

        val result = createAccountUseCase(addAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.EmptyAccountBalance)
        verifyNoInteractions(repository)
    }

    @Test
    fun `should return success when account is created`() = runTest {
        val addAccountState = AddAccountState(
            name = "BCA",
            type = AccountType.BANK,
            balance = 1_000_000
        )

        whenever(repository.createAccount(any(), any())).thenReturn(Unit)

        val result = createAccountUseCase(addAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.CreateAccountSuccess)

        verify(repository).createAccount(
            accountState = check {
                assertThat(it.name).isEqualTo("BCA")
                assertThat(it.type).isEqualTo(AccountType.BANK)
                assertThat(it.balance).isEqualTo(1_000_000)
                assertTrue(it.isActive)
            },
            transactionState = check {
                assertThat(it.title).isEqualTo("Penambahan Akun")
                assertThat(it.type).isEqualTo(TransactionType.INCOME)
                assertThat(it.amount).isEqualTo(1_000_000)
                assertThat(it.sourceName).isEqualTo("BCA")
                assertTrue(it.isLocked)
            }
        )
    }
}