package com.example.unitconverter.data

import com.example.unitconverter.BuildConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CurrencyRepository(private val api: CurrencyApi, private val dao: CurrencyDao) {

    private val apiKey = BuildConfig.API_KEY

    suspend fun getRates(): Flow<List<CurrencyRate>> = flow {
        val cached = dao.getAllRates()
        if (cached.isNotEmpty()) emit(cached)

        try {
            // 1. Fetch Full Names and Codes
            val codesResp = api.getSupportedCodes("v6/$apiKey/codes")
            val nameMap = codesResp.supported_codes.associate { it[0] to it[1] }

            // 2. Fetch Latest Rates
            val ratesResp = api.getLatestRates("v6/$apiKey/latest/USD")

            // 3. Merge them
            val mergedList = ratesResp.conversion_rates.map { (code, rate) ->
                CurrencyRate(
                    code = code,
                    fullName = nameMap[code] ?: "Global Currency",
                    rate = rate,
                    timestamp = System.currentTimeMillis()
                )
            }

            dao.insertRates(mergedList)
            emit(mergedList)
        } catch (e: Exception) {
            if (cached.isEmpty()) throw e
        }
    }
}