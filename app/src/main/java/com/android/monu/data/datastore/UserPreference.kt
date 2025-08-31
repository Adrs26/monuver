package com.android.monu.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")

class UserPreference(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_setting")
    }

    val themeSetting: Flow<ThemeSetting> = context.dataStore.data
        .map { preferences ->
            when (preferences[THEME_KEY]) {
                ThemeSetting.LIGHT.name -> ThemeSetting.LIGHT
                ThemeSetting.DARK.name -> ThemeSetting.DARK
                else -> ThemeSetting.SYSTEM
            }
        }

    suspend fun saveThemeSetting(themeSetting: ThemeSetting) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = themeSetting.name
        }
    }
}

enum class ThemeSetting {
    LIGHT, DARK, SYSTEM
}