package com.android.monu

import com.android.monu.domain.model.bill.Bill
import com.android.monu.domain.model.budget.Budget
import com.android.monu.domain.model.transaction.Transaction
import com.android.monu.domain.repository.AccountRepository
import com.android.monu.domain.repository.BudgetRepository
import com.android.monu.domain.repository.FinanceRepository
import com.android.monu.domain.usecase.finance.ProcessBillPaymentUseCase
import com.android.monu.ui.feature.screen.billing.payBill.components.PayBillContentState
import com.android.monu.ui.feature.utils.Cycle
import com.android.monu.ui.feature.utils.DatabaseResultMessage
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions

class PayBillUseCaseTest {

    private lateinit var financeRepository: FinanceRepository
    private lateinit var accountRepository: AccountRepository
    private lateinit var budgetRepository: BudgetRepository
    private lateinit var useCase: ProcessBillPaymentUseCase

    @Before
    fun setup() {
        financeRepository = mock()
        accountRepository = mock()
        budgetRepository = mock()
        useCase = ProcessBillPaymentUseCase(financeRepository, accountRepository, budgetRepository)
    }

    @Test
    fun `should return EmptyTransactionTitle when title is empty`() = runTest {
        val bill = Bill(
            id = 1,
            title = "Electricity",
            dueDate = "2025-09-14",
            paidDate = "2025-09-14",
            timeStamp = System.currentTimeMillis(),
            amount = 1000,
            isRecurring = true,
            cycle = 1,
            period = 1,
            fixPeriod = 0,
            isPaid = false,
            nowPaidPeriod = 1
        )

        val state = PayBillContentState(
            title = "",
            childCategory = 1,
            amount = 1000,
            date = "2025-09-14",
            sourceId = 1,
            sourceName = "Wallet",
            parentCategory = 1
        )

        val result = useCase(bill, state)

        assertEquals(DatabaseResultMessage.EmptyTransactionTitle, result)
        verifyNoInteractions(financeRepository)
    }

    @Test
    fun `should return InsufficientAccountBalance when balance is less than amount`() = runTest {
        val bill = Bill(
            id = 1,
            title = "Electricity",
            dueDate = "2025-09-14",
            paidDate = "2025-09-14",
            timeStamp = System.currentTimeMillis(),
            amount = 5000,
            isRecurring = true,
            cycle = 1,
            period = 1,
            fixPeriod = 0,
            isPaid = false,
            nowPaidPeriod = 1
        )

        val state = PayBillContentState(
            title = "Electricity",
            childCategory = 2,
            amount = 5000,
            date = "2025-09-14",
            sourceId = 1,
            sourceName = "Wallet",
            parentCategory = 1
        )

        `when`(accountRepository.getAccountBalance(1)).thenReturn(1000L)

        val result = useCase(bill, state)

        assertEquals(DatabaseResultMessage.InsufficientAccountBalance, result)
        verifyNoInteractions(financeRepository)
    }

    @Test
    fun `should return CurrentBudgetAmountExceedsMaximumLimit when budget exceeded and overflow not allowed`() = runTest {
        val bill = Bill(
            id = 1,
            title = "Electricity",
            dueDate = "2025-09-14",
            paidDate = "2025-09-14",
            timeStamp = System.currentTimeMillis(),
            amount = 1000,
            isRecurring = true,
            cycle = 1,
            period = 1,
            fixPeriod = 0,
            isPaid = false,
            nowPaidPeriod = 1
        )

        val state = PayBillContentState(
            title = "Rent",
            childCategory = 1,
            amount = 1000,
            date = "2025-09-14",
            sourceId = 1,
            sourceName = "Wallet",
            parentCategory = 1
        )

        `when`(accountRepository.getAccountBalance(1)).thenReturn(5000L)
        `when`(budgetRepository.getBudgetForDate(1, "2025-09-14")).thenReturn(
            Budget(
                id = 1,
                category = 1,
                cycle = Cycle.MONTHLY,
                startDate = "2025-09-01",
                endDate = "2025-09-30",
                maxAmount = 500,
                usedAmount = 0,
                isActive = true,
                isOverflowAllowed = false,
                isAutoUpdate = false
            )
        )

        val result = useCase(bill, state)

        assertEquals(DatabaseResultMessage.CurrentBudgetAmountExceedsMaximumLimit, result)
        verifyNoInteractions(financeRepository)
    }

    @Test
    fun `should pay bill successfully when all conditions met`() = runTest {
        val bill = Bill(
            id = 1,
            title = "Internet",
            dueDate = "2025-09-14",
            paidDate = "2025-09-14",
            timeStamp = System.currentTimeMillis(),
            amount = 1000,
            isRecurring = true,
            cycle = 1,
            period = 1,
            fixPeriod = 0,
            isPaid = false,
            nowPaidPeriod = 1
        )

        val state = PayBillContentState(
            title = "Internet",
            childCategory = 1,
            amount = 1000,
            date = "2025-09-14",
            sourceId = 1,
            sourceName = "Wallet",
            parentCategory = 1
        )

        `when`(accountRepository.getAccountBalance(1)).thenReturn(5000L)
        `when`(budgetRepository.getBudgetForDate(1, "2025-09-14")).thenReturn(null)

        val result = useCase(bill, state)

        assertEquals(DatabaseResultMessage.PayBillSuccess, result)

        val transactionCaptor = argumentCaptor<Transaction>()
        val billCaptor = argumentCaptor<Bill>()

        verify(financeRepository).payBill(
            eq(1),
            eq("2025-09-14"),
            transactionCaptor.capture(),
            eq(true),
            billCaptor.capture()
        )

        assertEquals(transactionCaptor.firstValue.amount, 1000)
        assertEquals(billCaptor.firstValue.nowPaidPeriod, 2)
    }
}
