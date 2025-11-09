package com.android.monu.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("monu_preferences")

class UserPreferences(private val context: Context) {

    companion object {
        private val NOTIFICATION_KEY = booleanPreferencesKey("notification")
        private val REMINDER_DAYS_BEFORE_DUE_KEY = intPreferencesKey("reminder_days_before_due")
        private val REMINDER_AFTER_DUE_DAY_KEY = booleanPreferencesKey("reminder_after_due_day")
        private val REMINDER_FOR_DUE_BILL_KEY = booleanPreferencesKey("reminder_for_due_bill")
        private val THEME_KEY = stringPreferencesKey("theme_setting")
        private val FIRST_EXPORT_KEY = booleanPreferencesKey("first_export")
        private val FIRST_BACKUP_KEY = booleanPreferencesKey("first_backup")
        private val FIRST_RESTORE_KEY = booleanPreferencesKey("first_restore")
        private val AUTHENTICATION_KEY = booleanPreferencesKey("authentication")
    }

    val isNotificationEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[NOTIFICATION_KEY] ?: false
        }

    val reminderDaysBeforeDue: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[REMINDER_DAYS_BEFORE_DUE_KEY] ?: 1
        }

    val isReminderBeforeDueDayEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[REMINDER_AFTER_DUE_DAY_KEY] ?: false
        }

    val isReminderForDueBillEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[REMINDER_FOR_DUE_BILL_KEY] ?: true
        }

    val themeSetting: Flow<ThemeSetting> = context.dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY]) {
                ThemeSetting.LIGHT.name -> ThemeSetting.LIGHT
                ThemeSetting.DARK.name -> ThemeSetting.DARK
                else -> ThemeSetting.SYSTEM
            }
        }

    val isFirstExport: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_EXPORT_KEY] ?: true
        }

    val isFirstBackup: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_BACKUP_KEY] ?: true
        }

    val isFirstRestore: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[FIRST_RESTORE_KEY] ?: true
        }

    val isAuthenticationEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[AUTHENTICATION_KEY] ?: false
        }

    suspend fun setNotification(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATION_KEY] = isEnabled
        }
    }

    suspend fun setReminderSettings(
        reminderDaysBeforeDue: Int,
        isReminderBeforeDueDayEnabled: Boolean,
        isReminderForDueBillEnabled: Boolean
    ) {
        context.dataStore.edit { preferences ->
            preferences[REMINDER_DAYS_BEFORE_DUE_KEY] = reminderDaysBeforeDue
            preferences[REMINDER_AFTER_DUE_DAY_KEY] = isReminderBeforeDueDayEnabled
            preferences[REMINDER_FOR_DUE_BILL_KEY] = isReminderForDueBillEnabled
        }
    }

    suspend fun saveThemeSetting(themeSetting: ThemeSetting) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeSetting.name
        }
    }

    suspend fun setIsFirstExportToFalse() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_EXPORT_KEY] = false
        }
    }

    suspend fun setIsFirstBackupToFalse() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_BACKUP_KEY] = false
        }
    }

    suspend fun setIsFirstRestoreToFalse() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_RESTORE_KEY] = false
        }
    }

    suspend fun setAuthentication(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTHENTICATION_KEY] = isEnabled
        }
    }
}

enum class ThemeSetting {
    LIGHT, DARK, SYSTEM
}