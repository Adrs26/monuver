package com.android.monu.presentation.utils

import androidx.compose.ui.graphics.Color
import com.android.monu.R
import com.android.monu.ui.theme.Amber500
import com.android.monu.ui.theme.Blue400
import com.android.monu.ui.theme.Blue800
import com.android.monu.ui.theme.BlueGrey400
import com.android.monu.ui.theme.Brown400
import com.android.monu.ui.theme.DeepPurple400
import com.android.monu.ui.theme.Green400
import com.android.monu.ui.theme.Green600
import com.android.monu.ui.theme.Green800
import com.android.monu.ui.theme.Indigo400
import com.android.monu.ui.theme.Orange600
import com.android.monu.ui.theme.Pink400
import com.android.monu.ui.theme.Teal400
import com.android.monu.ui.theme.Teal600

object DatabaseCodeMapper {
    fun toAccountType(code: Int): Int {
        return when (code) {
            AccountType.CASH -> R.string.cash
            AccountType.BANK -> R.string.bank
            AccountType.E_WALLET -> R.string.e_wallet
            AccountType.CREDIT_CARD -> R.string.credit_card
            AccountType.INVESTMENT -> R.string.investment
            else -> 0
        }
    }

    fun toAccountTypeIcon(code: Int): Int {
        return when (code) {
            AccountType.CASH -> R.drawable.ic_payments
            AccountType.BANK -> R.drawable.ic_account_balance
            AccountType.E_WALLET -> R.drawable.ic_wallet
            AccountType.CREDIT_CARD -> R.drawable.ic_credit_card
            AccountType.INVESTMENT -> R.drawable.ic_bar_chart
            else -> 0
        }
    }

    fun toAccountTypeColor(code: Int): Color {
        return when (code) {
            AccountType.CASH -> Green400
            AccountType.BANK -> Indigo400
            AccountType.E_WALLET -> Blue400
            AccountType.CREDIT_CARD -> Brown400
            AccountType.INVESTMENT -> Amber500
            else -> Color.White
        }
    }

    fun toTransactionType(code: Int): Int {
        return when (code) {
            TransactionType.ALL -> R.string.all
            TransactionType.INCOME -> R.string.income
            TransactionType.EXPENSE -> R.string.expense
            TransactionType.TRANSFER -> R.string.transfer
            else -> 0
        }
    }

    fun toParentCategoryTitle(code: Int): Int {
        return when (code) {
            TransactionParentCategory.SALARY -> R.string.salary
            TransactionParentCategory.BONUSES -> R.string.bonuses
            TransactionParentCategory.COMMISSION -> R.string.commission
            TransactionParentCategory.INVESTMENT_RESULT -> R.string.investment_result
            TransactionParentCategory.OTHER_INCOME -> R.string.other_income
            TransactionParentCategory.FOOD_BEVERAGES -> R.string.food_beverages
            TransactionParentCategory.BILLS_UTILITIES -> R.string.bills_utilities
            TransactionParentCategory.TRANSPORTATION -> R.string.transportation
            TransactionParentCategory.HEALTH_PERSONAL_CARE -> R.string.health_personal_care
            TransactionParentCategory.EDUCATION -> R.string.education
            TransactionParentCategory.SHOPPING -> R.string.shopping
            TransactionParentCategory.ENTERTAINMENT -> R.string.entertainment
            TransactionParentCategory.OTHER_EXPENSE -> R.string.other_expense
            TransactionParentCategory.TRANSFER -> R.string.transfer
            else -> R.string.income
        }
    }

    fun toChildCategoryTitle(code: Int): Int {
        return when (code) {
            TransactionChildCategory.SALARY -> R.string.salary
            TransactionChildCategory.BONUSES -> R.string.bonuses
            TransactionChildCategory.COMMISSION -> R.string.commission
            TransactionChildCategory.INVESTMENT_RESULT -> R.string.investment_result
            TransactionChildCategory.OTHER_INCOME -> R.string.other_income

            TransactionChildCategory.FOOD -> R.string.food
            TransactionChildCategory.DRINK -> R.string.drink
            TransactionChildCategory.TEA_COFFEE -> R.string.tea_coffee
            TransactionChildCategory.GROCERY -> R.string.grocery

            TransactionChildCategory.WATER -> R.string.water
            TransactionChildCategory.ELECTRICITY -> R.string.electricity
            TransactionChildCategory.GAS -> R.string.gas
            TransactionChildCategory.TAX -> R.string.tax
            TransactionChildCategory.HOUSE -> R.string.house
            TransactionChildCategory.INTERNET -> R.string.internet

            TransactionChildCategory.MAINTENANCE -> R.string.maintenance
            TransactionChildCategory.FUEL -> R.string.fuel
            TransactionChildCategory.VEHICLE_ACCESSORIES -> R.string.accessories
            TransactionChildCategory.ONLINE_RIDE -> R.string.online_ride
            TransactionChildCategory.PUBLIC_TRANSPORT -> R.string.public_transport

            TransactionChildCategory.HOSPITAL -> R.string.hospital
            TransactionChildCategory.DOCTOR -> R.string.doctor
            TransactionChildCategory.MEDICINE -> R.string.medicine
            TransactionChildCategory.PERSONAL_CARE -> R.string.personal_care
            TransactionChildCategory.MASSAGE -> R.string.massage
            TransactionChildCategory.SPA -> R.string.spa
            TransactionChildCategory.GYM -> R.string.gym

            TransactionChildCategory.EDUCATION_FEE -> R.string.education_fee
            TransactionChildCategory.BOOKS_STATIONERY -> R.string.books_stationery
            TransactionChildCategory.COURSE -> R.string.course
            TransactionChildCategory.PRINT_COPY -> R.string.print_copy

            TransactionChildCategory.CLOTHING -> R.string.clothing
            TransactionChildCategory.SHOES -> R.string.shoes
            TransactionChildCategory.BAG -> R.string.bag
            TransactionChildCategory.ACCESSORIES -> R.string.accessories
            TransactionChildCategory.ELECTRONICS -> R.string.electronics
            TransactionChildCategory.FURNITURE -> R.string.furniture
            TransactionChildCategory.VEHICLE -> R.string.vehicle

            TransactionChildCategory.DIGITAL_SUBSCRIPTION -> R.string.digital_subscription
            TransactionChildCategory.CINEMA -> R.string.cinema
            TransactionChildCategory.GAMES -> R.string.games
            TransactionChildCategory.CONCERT_FESTIVAL -> R.string.concert_festival
            TransactionChildCategory.BOOKS_COMICS -> R.string.books_comics
            TransactionChildCategory.HOBBIES_COLLECTIONS -> R.string.hobbies_collections
            TransactionChildCategory.COMMUNITY -> R.string.community

            TransactionChildCategory.DONATION -> R.string.donation
            TransactionChildCategory.INSURANCE -> R.string.insurance
            TransactionChildCategory.INVESTMENT -> R.string.investment
            TransactionChildCategory.OTHER_EXPENSE -> R.string.other_expense

            TransactionChildCategory.TRANSFER_ACCOUNT -> R.string.transfer_account
            TransactionChildCategory.SAVINGS_IN -> R.string.savings_in
            TransactionChildCategory.SAVINGS_OUT -> R.string.savings_out

            else -> 0
        }
    }

    fun toChildCategoryIcon(code: Int): Int {
        return when (code) {
            TransactionChildCategory.SALARY -> R.drawable.ic_work
            TransactionChildCategory.BONUSES -> R.drawable.ic_star
            TransactionChildCategory.COMMISSION -> R.drawable.ic_attach_money
            TransactionChildCategory.INVESTMENT_RESULT -> R.drawable.ic_finance_mode
            TransactionChildCategory.OTHER_INCOME -> R.drawable.ic_more_horiz

            TransactionChildCategory.FOOD -> R.drawable.ic_fork_spoon
            TransactionChildCategory.DRINK -> R.drawable.ic_water_full
            TransactionChildCategory.TEA_COFFEE -> R.drawable.ic_coffee
            TransactionChildCategory.GROCERY -> R.drawable.ic_shopping_basket

            TransactionChildCategory.WATER -> R.drawable.ic_water_drop
            TransactionChildCategory.ELECTRICITY -> R.drawable.ic_electric_bolt
            TransactionChildCategory.GAS -> R.drawable.ic_propane_tank
            TransactionChildCategory.TAX -> R.drawable.ic_request_quote
            TransactionChildCategory.HOUSE -> R.drawable.ic_house
            TransactionChildCategory.INTERNET -> R.drawable.ic_wifi

            TransactionChildCategory.MAINTENANCE -> R.drawable.ic_car_repair
            TransactionChildCategory.FUEL -> R.drawable.ic_local_gas_station
            TransactionChildCategory.VEHICLE_ACCESSORIES -> R.drawable.ic_car_gear
            TransactionChildCategory.ONLINE_RIDE -> R.drawable.ic_transportation
            TransactionChildCategory.PUBLIC_TRANSPORT -> R.drawable.ic_directions_bus

            TransactionChildCategory.HOSPITAL -> R.drawable.ic_home_health
            TransactionChildCategory.DOCTOR -> R.drawable.ic_clinical_notes
            TransactionChildCategory.MEDICINE -> R.drawable.ic_pill
            TransactionChildCategory.PERSONAL_CARE -> R.drawable.ic_health_and_beauty
            TransactionChildCategory.MASSAGE -> R.drawable.ic_massage
            TransactionChildCategory.SPA -> R.drawable.ic_spa
            TransactionChildCategory.GYM -> R.drawable.ic_fitness_center

            TransactionChildCategory.EDUCATION_FEE -> R.drawable.ic_school
            TransactionChildCategory.BOOKS_STATIONERY -> R.drawable.ic_book
            TransactionChildCategory.COURSE -> R.drawable.ic_interactive_space
            TransactionChildCategory.PRINT_COPY -> R.drawable.ic_print

            TransactionChildCategory.CLOTHING -> R.drawable.ic_apparel
            TransactionChildCategory.SHOES -> R.drawable.ic_podiatry
            TransactionChildCategory.BAG -> R.drawable.ic_backpack
            TransactionChildCategory.ACCESSORIES -> R.drawable.ic_eyeglasses
            TransactionChildCategory.ELECTRONICS -> R.drawable.ic_devices
            TransactionChildCategory.FURNITURE -> R.drawable.ic_chair
            TransactionChildCategory.VEHICLE -> R.drawable.ic_search_hands_free

            TransactionChildCategory.DIGITAL_SUBSCRIPTION -> R.drawable.ic_subscriptions
            TransactionChildCategory.CINEMA -> R.drawable.ic_movie
            TransactionChildCategory.GAMES -> R.drawable.ic_sports_esports
            TransactionChildCategory.CONCERT_FESTIVAL -> R.drawable.ic_festival
            TransactionChildCategory.BOOKS_COMICS -> R.drawable.ic_manga
            TransactionChildCategory.HOBBIES_COLLECTIONS -> R.drawable.ic_interests
            TransactionChildCategory.COMMUNITY -> R.drawable.ic_groups

            TransactionChildCategory.DONATION -> R.drawable.ic_volunteer_activism
            TransactionChildCategory.INSURANCE -> R.drawable.ic_shield_with_heart
            TransactionChildCategory.INVESTMENT -> R.drawable.ic_finance_mode
            TransactionChildCategory.OTHER_EXPENSE -> R.drawable.ic_more_horiz

            TransactionChildCategory.TRANSFER_ACCOUNT -> R.drawable.ic_compare_arrows
            TransactionChildCategory.SAVINGS_IN -> R.drawable.ic_savings_in
            TransactionChildCategory.SAVINGS_OUT -> R.drawable.ic_savings_out

            else -> 0
        }
    }

    fun toParentCategoryIconBackground(code: Int): Color {
        return when (code) {
            1 -> Green600
            2 -> Green400
            3 -> Teal400
            4 -> Teal600
            5 -> Green800
            6 -> Orange600
            7 -> Indigo400
            8 -> BlueGrey400
            9 -> Blue400
            10 -> Amber500
            11 -> Pink400
            12 -> DeepPurple400
            13 -> Brown400
            else -> Blue800
        }
    }

    fun toFullMonth(code: Int): Int {
        return when (code) {
            1 -> R.string.january
            2 -> R.string.february
            3 -> R.string.march
            4 -> R.string.april
            5 -> R.string.may_full
            6 -> R.string.june
            7 -> R.string.july
            8 -> R.string.august
            9 -> R.string.september
            10 -> R.string.october
            11 -> R.string.november
            12 -> R.string.december
            else -> 0
        }
    }

    fun toShortMonth(code: Int): Int {
        return when (code) {
            Month.ALL -> R.string.all
            Month.JANUARY -> R.string.jan
            Month.FEBRUARY -> R.string.feb
            Month.MARCH -> R.string.mar
            Month.APRIL -> R.string.apr
            Month.MAY -> R.string.may_short
            Month.JUNE -> R.string.jun
            Month.JULY -> R.string.jul
            Month.AUGUST -> R.string.aug
            Month.SEPTEMBER -> R.string.sep
            Month.OCTOBER -> R.string.oct
            Month.NOVEMBER -> R.string.nov
            Month.DECEMBER -> R.string.dec
            else -> 0
        }
    }

    fun toBudgetingPeriod(code: Int): Int {
        return when (code) {
            BudgetingPeriod.MONTHLY -> R.string.monthly
            BudgetingPeriod.WEEKLY -> R.string.weekly
            else -> R.string.custom
        }
    }
}