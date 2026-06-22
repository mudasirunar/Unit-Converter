package com.example.unitconverter.data

data class ConversionUnit(
    val name: String,
    val factor: Double // Conversion factor to/from the base unit
)

data class ConversionCategory(
    val title: String,
    val units: List<ConversionUnit>,
    val isTemperature: Boolean = false
)

fun getConversionData(): List<ConversionCategory> {
    return listOf(
        ConversionCategory(
            title = "Length",
            units = listOf(
                ConversionUnit("Meters", 1.0), // Base
                ConversionUnit("Kilometers", 1000.0),
                ConversionUnit("Centimeters", 0.01),
                ConversionUnit("Millimeters", 0.001),
                ConversionUnit("Miles", 1609.34),
                ConversionUnit("Yards", 0.9144),
                ConversionUnit("Feet", 0.3048),
                ConversionUnit("Inches", 0.0254)
            )
        ),
        ConversionCategory(
            title = "Weight",
            units = listOf(
                ConversionUnit("Kilograms", 1.0), // Base
                ConversionUnit("Grams", 0.001),
                ConversionUnit("Pounds", 0.453592),
                ConversionUnit("Ounces", 0.0283495),
                ConversionUnit("Tons", 907.185)
            )
        ),
        ConversionCategory(
            title = "Temperature",
            units = listOf(
                ConversionUnit("Celsius", 1.0),
                ConversionUnit("Fahrenheit", 1.0),
                ConversionUnit("Kelvin", 1.0)
            ),
            isTemperature = true
        ),
        ConversionCategory(
            title = "Area",
            units = listOf(
                ConversionUnit("Square Meters", 1.0), // Base
                ConversionUnit("Square Kilometers", 1000000.0),
                ConversionUnit("Square Miles", 2589988.11),
                ConversionUnit("Acres", 4046.86),
                ConversionUnit("Hectares", 10000.0)
            )
        ),
        ConversionCategory(
            title = "Volume",
            units = listOf(
                ConversionUnit("Liters", 1.0), // Base
                ConversionUnit("Milliliters", 0.001),
                ConversionUnit("Gallons", 3.78541),
                ConversionUnit("Cups", 0.236588),
                ConversionUnit("Cubic Meters", 1000.0)
            )
        )
    )
}

fun convertUnits(
    amount: Double,
    from: ConversionUnit,
    to: ConversionUnit,
    isTemperature: Boolean
): Double {
    if (isTemperature) {
        val celsius = when (from.name) {
            "Celsius" -> amount
            "Fahrenheit" -> (amount - 32) / 1.8
            "Kelvin" -> amount - 273.15
            else -> amount
        }
        return when (to.name) {
            "Celsius" -> celsius
            "Fahrenheit" -> (celsius * 1.8) + 32
            "Kelvin" -> celsius + 273.15
            else -> celsius
        }
    } else {
        // Standard conversions: value -> base unit -> target unit
        val valueInBase = amount * from.factor
        return valueInBase / to.factor
    }
}
