package com.example.unitconverter

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unitconverter.data.AppDatabase
import com.example.unitconverter.data.CurrencyApi
import com.example.unitconverter.data.CurrencyRepository
import com.example.unitconverter.data.CurrencyViewModel
import com.example.unitconverter.data.UnitConverterViewModel
import com.example.unitconverter.ui.ConverterApp
import com.example.unitconverter.ui.components.GradientBackground
import com.example.unitconverter.ui.theme.UnitConverterTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_UnitConverter)
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        val sharedPrefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Initialize Room Database & Retrofit Network Client
        val db = AppDatabase.getInstance(applicationContext)
        val api = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)

        val repository = CurrencyRepository(api, db.currencyDao())
        
        setContent {
            // Detect if the device is currently in Dark Theme
            val isSystemDark = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == 
                    android.content.res.Configuration.UI_MODE_NIGHT_YES

            // Live theme state synced with SharedPreferences, defaulting to system theme on first launch
            var isDarkTheme by remember {
                mutableStateOf(sharedPrefs.getBoolean("dark_theme", isSystemDark))
            }

            UnitConverterTheme(darkTheme = isDarkTheme) {
                val viewModel: CurrencyViewModel = ViewModelProvider(
                    this,
                    CurrencyViewModelFactory(repository)
                )[CurrencyViewModel::class.java]
                val unitViewModel: UnitConverterViewModel = ViewModelProvider(this)[UnitConverterViewModel::class.java]

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GradientBackground(isDarkTheme = isDarkTheme) {
                        ConverterApp(
                            viewModel = viewModel,
                            unitViewModel = unitViewModel,
                            isDarkTheme = isDarkTheme,
                            onThemeChanged = { dark ->
                                isDarkTheme = dark
                                sharedPrefs.edit().putBoolean("dark_theme", dark).apply()
                            }
                        )
                    }
                }
            }
        }
    }
}

// Factory to inject repository into CurrencyViewModel
class CurrencyViewModelFactory(private val repository: CurrencyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CurrencyViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CurrencyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}