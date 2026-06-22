package com.example.unitconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unitconverter.data.AppDatabase
import com.example.unitconverter.data.CurrencyApi
import com.example.unitconverter.data.CurrencyRepository
import com.example.unitconverter.data.CurrencyViewModel
import com.example.unitconverter.ui.ConverterApp
import com.example.unitconverter.ui.components.GradientBackground
import com.example.unitconverter.ui.theme.UnitConverterTheme
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Room Database & Retrofit Network Client
        val db = AppDatabase.getInstance(applicationContext)
        val api = Retrofit.Builder()
            .baseUrl("https://v6.exchangerate-api.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrencyApi::class.java)

        val repository = CurrencyRepository(api, db.currencyDao())
        
        setContent {
            UnitConverterTheme {
                val viewModel: CurrencyViewModel = ViewModelProvider(
                    this,
                    CurrencyViewModelFactory(repository)
                )[CurrencyViewModel::class.java]

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GradientBackground {
                        ConverterApp(viewModel)
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