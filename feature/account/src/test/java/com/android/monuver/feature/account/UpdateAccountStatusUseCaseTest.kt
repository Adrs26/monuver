package com.android.monuver.feature.account

import com.android.monuver.core.domain.common.DatabaseResultState
import com.android.monuver.feature.account.domain.repository.AccountRepository
import com.android.monuver.feature.account.domain.usecase.UpdateAccountStatusUseCase
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UpdateAccountStatusUseCaseTest {

    private lateinit var accountRepository: AccountRepository
    private lateinit var updateAccountStatusUseCase: UpdateAccountStatusUseCase

    @Before
    fun setup() {
        accountRepository = mock(AccountRepository::class.java)
        updateAccountStatusUseCase = UpdateAccountStatusUseCase(accountRepository)
    }

    @Test
    fun `should return activate account success when account active`() = runTest {
        whenever(accountRepository.updateAccountStatus(any(), any())).thenReturn(Unit)

        val result = updateAccountStatusUseCase(accountId = 1, isActive = true)

        assertThat(result).isEqualTo(DatabaseResultState.ActivateAccountSuccess)
        verify(accountRepository).updateAccountStatus(accountId = eq(1), isActive = eq(true))
    }

    @Test
    fun `should return deactivate account success when account inactive`() = runTest {
        whenever(accountRepository.updateAccountStatus(any(), any())).thenReturn(Unit)

        val result = updateAccountStatusUseCase(accountId = 1, isActive = false)

        assertThat(result).isEqualTo(DatabaseResultState.DeactivateAccountSuccess)
        verify(accountRepository).updateAccountStatus(accountId = eq(1), isActive = eq(false))
    }
}