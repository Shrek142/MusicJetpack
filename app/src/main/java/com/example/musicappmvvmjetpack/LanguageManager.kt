package com.example.musicappmvvmjetpack

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import java.util.Locale

object LanguageManager {
    fun setLocale(context: Context, language: String): Context {
        Log.d("LanguageDebug", "Setting locale to: $language")
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        return context.createConfigurationContext(config)
    }
}
