package com.android.monu.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("monu_preferences")

class UserPreference(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_setting")
        private val FIRST_EXPORT_KEY = booleanPreferencesKey("first_export")
        private val FIRST_BACKUP_KEY = booleanPreferencesKey("first_backup")
        private val FIRST_RESTORE_KEY = booleanPreferencesKey("first_restore")
        private val AUTHENTICATION_KEY = booleanPreferencesKey("authentication")
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

    suspend fun setAuthenticationEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[AUTHENTICATION_KEY] = enabled
        }
    }
}

enum class ThemeSetting {
    LIGHT, DARK, SYSTEM
}