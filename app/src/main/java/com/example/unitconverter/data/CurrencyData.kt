package com.example.unitconverter.data

import androidx.room.*
import retrofit2.http.GET
import retrofit2.http.Url

// --- Room Entity ---
@Entity(tableName = "currency_rates")
data class CurrencyRate(
    @PrimaryKey val code: String,
    val fullName: String, // Added to store the full name (e.g., Pakistani Rupee)
    val rate: Double,
    val timestamp: Long
)

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM currency_rates")
    suspend fun getAllRates(): List<CurrencyRate>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: List<CurrencyRate>)
}

// --- API Responses ---
data class ExchangeRateResponse(
    val conversion_rates: Map<String, Double>
)

data class CurrencyCodesResponse(
    val supported_codes: List<List<String>> // Returns [["USD", "United States Dollar"], ...]
)

interface CurrencyApi {
    @GET
    suspend fun getLatestRates(@Url url: String): ExchangeRateResponse

    @GET
    suspend fun getSupportedCodes(@Url url: String): CurrencyCodesResponse
}