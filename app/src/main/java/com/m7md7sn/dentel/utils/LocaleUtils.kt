package com.m7md7sn.dentel.utils

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleUtils {
    private const val LANGUAGE_PREF = "language_preference"
    private const val SELECTED_LANGUAGE_KEY = "selected_language"

    /**
     * Updates the app locale and persists the selection
     *
     * @param context The application context
     * @param language The language code (e.g. "en", "ar")
     * @return Context with updated configuration
     */
    fun setLocale(context: Context, language: String): Context {
        // Save language preference
        saveLanguagePref(context, language)

        // Set default locale
        val locale = Locale(language)
        Locale.setDefault(locale)

        // Update configuration
        val config = Configuration(context.resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale)
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        } else {
            @Suppress("DEPRECATION")
            config.locale = locale
            @Suppress("DEPRECATION")
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.createConfigurationContext(config)
        } else {
            context
        }
    }

    /**
     * Apply the saved language to the given context
     * Call this in attachBaseContext() of Application class
     */
    fun applyLocale(context: Context): Context {
        val language = getSavedLanguage(context)
        return if (language != null) {
            // Apply saved language
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                config.setLocale(locale)
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            } else {
                @Suppress("DEPRECATION")
                config.locale = locale
                @Suppress("DEPRECATION")
                context.resources.updateConfiguration(config, context.resources.displayMetrics)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                context.createConfigurationContext(config)
            } else {
                context
            }
        } else {
            // No saved language, use current context
            context
        }
    }

    /**
     * Save selected language to SharedPreferences
     */
    private fun saveLanguagePref(context: Context, language: String) {
        val sharedPref = getSharedPreferences(context)
        with(sharedPref.edit()) {
            putString(SELECTED_LANGUAGE_KEY, language)
            apply()
        }
    }

    /**
     * Get the saved language from SharedPreferences
     * @return language code or null if not set
     */
    fun getSavedLanguage(context: Context): String? {
        val sharedPref = getSharedPreferences(context)
        return sharedPref.getString(SELECTED_LANGUAGE_KEY, null)
    }

    /**
     * Get language name for display from locale
     */
    fun getDisplayLanguage(locale: Locale): String {
        return when (locale.language) {
            "ar" -> "Arabic"
            else -> "English"
        }
    }

    /**
     * Get SharedPreferences instance
     */
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(LANGUAGE_PREF, Context.MODE_PRIVATE)
    }
}
