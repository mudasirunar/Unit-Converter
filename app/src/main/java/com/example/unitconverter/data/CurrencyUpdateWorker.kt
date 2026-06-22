package com.example.unitconverter.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.unitconverter.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val db = AppDatabase.getInstance(applicationContext)

            // Initialize API with the Base URL for ExchangeRate-API
            val api = Retrofit.Builder()
                .baseUrl("https://v6.exchangerate-api.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CurrencyApi::class.java)

            // Use your key to build the dynamic URL
            val apiKey = BuildConfig.API_KEY
            val fullUrl = "v6/$apiKey/latest/USD"

            // Fetch fresh rates
            val response = api.getLatestRates(fullUrl)

            // Map conversion_rates to our CurrencyRate Entity
            val codesResp = api.getSupportedCodes("v6/$apiKey/codes")
            val nameMap = codesResp.supported_codes.associate { it[0] to it[1] }
            val ratesResp = api.getLatestRates("v6/$apiKey/latest/USD")

            val newRates = ratesResp.conversion_rates.map { (code, rate) ->
                CurrencyRate(code, nameMap[code] ?: "Global", rate, System.currentTimeMillis())
            }
            db.currencyDao().insertRates(newRates)

            Result.success()
        } catch (e: Exception) {
            // Log the error if needed: android.util.Log.e("Worker", "Error", e)
            Result.retry()
        }
    }
}