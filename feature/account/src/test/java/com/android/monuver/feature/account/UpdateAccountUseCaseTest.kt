package com.android.monuver.feature.account

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.core.domain.util.AccountType
import com.android.monuver.feature.account.domain.model.EditAccountState
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.UpdateAccountUseCase
import com.google.common.truth.Truth.assertThat
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

class UpdateAccountUseCaseTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var updateAccountUseCase: UpdateAccountUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        updateAccountUseCase = UpdateAccountUseCase(accountRepository)
    }

    @Test
    fun `should return error when account name is empty`() = runTest {
        val editAccountState = EditAccountState(
            id = 1,
            name = "",
            type = AccountType.BANK,
            balance = 1_000_000
        )

        val result = updateAccountUseCase(editAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.EmptyAccountName)
        verifyNoInteractions(accountRepository)
    }

    @Test
    fun `should return error when account type is empty`() = runTest {
        val editAccountState = EditAccountState(
            id = 1,
            name = "BCA",
            type = 0,
            balance = 1_000_000
        )

        val result = updateAccountUseCase(editAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.EmptyAccountType)
        verifyNoInteractions(accountRepository)
    }

    @Test
    fun `should return success when account is updated`() = runTest {
        val editAccountState = EditAccountState(
            id = 1,
            name = "BCA",
            type = AccountType.BANK,
            balance = 1_000_000
        )

        whenever(accountRepository.updateAccount(any())).thenReturn(Unit)

        val result = updateAccountUseCase(editAccountState)

        assertThat(result).isEqualTo(DatabaseResultState.UpdateAccountSuccess)

        verify(accountRepository).updateAccount(
            accountState = check {
                assertEquals(1, it.id)
                assertEquals("BCA", it.name)
                assertEquals(AccountType.BANK, it.type)
                assertEquals(1_000_000, it.balance)
                assertTrue(it.isActive)
            }
        )
    }
}