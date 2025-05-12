package com.example.musicappmvvmjetpack

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.musicappmvvmjetpack.Activities.MainScreen
import com.example.musicappmvvmjetpack.Activities.theme.MusicAppMVVMJetpackTheme

class MainActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val language = prefs.getString("lang", "vi") ?: "vi"
        val updatedContext = LanguageManager.setLocale(newBase, language)
        super.attachBaseContext(updatedContext)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MusicAppMVVMJetpackTheme {
                MainScreen()
            }
        }
    }
}