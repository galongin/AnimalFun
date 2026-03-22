package com.animalfun.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object LocaleHelper {

    private val LANGUAGE_KEY = stringPreferencesKey("language")

    const val ENGLISH = "en"
    const val HEBREW = "iw"

    fun getLanguageFlow(context: Context): Flow<String> {
        return context.settingsDataStore.data.map { preferences ->
            preferences[LANGUAGE_KEY] ?: ENGLISH
        }
    }

    suspend fun setLanguage(context: Context, languageCode: String) {
        context.settingsDataStore.edit { preferences ->
            preferences[LANGUAGE_KEY] = languageCode
        }
        applyLanguage(languageCode)
    }

    fun applyLanguage(languageCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun getCurrentLanguage(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        if (locales.isEmpty) return ENGLISH
        val tag = locales.get(0)?.language ?: ENGLISH
        return if (tag == "he" || tag == "iw") HEBREW else ENGLISH
    }

    fun isHebrew(): Boolean = getCurrentLanguage() == HEBREW

    fun getLayoutDirection(): LayoutDirection {
        return if (isHebrew()) LayoutDirection.Rtl else LayoutDirection.Ltr
    }
}

@Composable
fun ProvideLayoutDirection(
    language: String,
    content: @Composable () -> Unit
) {
    val direction = if (language == LocaleHelper.HEBREW) {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides direction,
        content = content
    )
}
