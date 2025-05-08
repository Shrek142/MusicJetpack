package com.example.musicappmvvmjetpack

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.musicappmvvmjetpack.Activities.ScreenNavigation
import com.example.musicappmvvmjetpack.Activities.theme.MusicAppMVVMJetpackTheme

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("lang", "vi") ?: "vi"
        Log.d("LanguageDebug", "Applying language from SharedPreferences: $language")
        val updatedContext = LanguageManager.setLocale(newBase, language)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppMVVMJetpackTheme {
                ScreenNavigation()
            }
        }
    }
}
