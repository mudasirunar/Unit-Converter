package com.example.unitconverter.data

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

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
                ConversionUnit("Nanometers", 1e-9),
                ConversionUnit("Micrometers", 1e-6),
                ConversionUnit("Millimeters", 0.001),
                ConversionUnit("Centimeters", 0.01),
                ConversionUnit("Inches", 0.0254),
                ConversionUnit("Feet", 0.3048),
                ConversionUnit("Yards", 0.9144),
                ConversionUnit("Meters", 1.0), // Base
                ConversionUnit("Kilometers", 1000.0),
                ConversionUnit("Miles", 1609.344)
            )
        ),
        ConversionCategory(
            title = "Weight",
            units = listOf(
                ConversionUnit("Milligrams", 1e-6),
                ConversionUnit("Grams", 0.001),
                ConversionUnit("Ounces", 0.028349523),
                ConversionUnit("Pounds", 0.45359237),
                ConversionUnit("Kilograms", 1.0), // Base
                ConversionUnit("Stones", 6.35029318),
                ConversionUnit("Tons (Metric)", 1000.0)
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
                ConversionUnit("Sq Inches", 0.00064516),
                ConversionUnit("Sq Feet", 0.09290304),
                ConversionUnit("Sq Yards", 0.83612736),
                ConversionUnit("Sq Meters", 1.0), // Base
                ConversionUnit("Acres", 4046.85642),
                ConversionUnit("Hectares", 10000.0),
                ConversionUnit("Sq Kilometers", 1000000.0),
                ConversionUnit("Sq Miles", 2589988.11)
            )
        ),
        ConversionCategory(
            title = "Volume",
            units = listOf(
                ConversionUnit("Milliliters", 0.001),
                ConversionUnit("Fluid Ounces (US)", 0.02957353),
                ConversionUnit("Cups (US)", 0.23658824),
                ConversionUnit("Liters", 1.0), // Base
                ConversionUnit("Gallons (US)", 3.78541178),
                ConversionUnit("Cubic Feet", 28.3168466),
                ConversionUnit("Cubic Meters", 1000.0)
            )
        ),
        ConversionCategory(
            title = "Storage",
            units = listOf(
                ConversionUnit("Bits", 0.125),
                ConversionUnit("Bytes", 1.0), // Base
                ConversionUnit("Kilobits (Kb)", 128.0),
                ConversionUnit("Kilobytes (KB)", 1024.0),
                ConversionUnit("Megabits (Mb)", 131072.0),
                ConversionUnit("Megabytes (MB)", 1024.0 * 1024.0),
                ConversionUnit("Gigabits (Gb)", 134217728.0),
                ConversionUnit("Gigabytes (GB)", 1024.0 * 1024.0 * 1024.0),
                ConversionUnit("Terabits (Tb)", 1.37438953472e11),
                ConversionUnit("Terabytes (TB)", 1024.0 * 1024.0 * 1024.0 * 1024.0),
                ConversionUnit("Petabytes (PB)", 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0)
            )
        ),
        ConversionCategory(
            title = "Time",
            units = listOf(
                ConversionUnit("Nanoseconds", 1e-9),
                ConversionUnit("Microseconds", 1e-6),
                ConversionUnit("Milliseconds", 0.001),
                ConversionUnit("Seconds", 1.0), // Base
                ConversionUnit("Minutes", 60.0),
                ConversionUnit("Hours", 3600.0),
                ConversionUnit("Days", 86400.0),
                ConversionUnit("Weeks", 604800.0),
                ConversionUnit("Months (Avg)", 2.628e6),
                ConversionUnit("Years (365d)", 3.1536e7)
            )
        ),
        ConversionCategory(
            title = "Speed",
            units = listOf(
                ConversionUnit("m/s", 1.0), // Base
                ConversionUnit("km/h", 1.0 / 3.6),
                ConversionUnit("mph", 0.44704),
                ConversionUnit("Knots", 0.51444444)
            )
        ),
        ConversionCategory(
            title = "Energy",
            units = listOf(
                ConversionUnit("Electronvolts (eV)", 1.602176634e-19),
                ConversionUnit("Joules (J)", 1.0), // Base
                ConversionUnit("Calories (cal)", 4.184),
                ConversionUnit("Kilojoules (kJ)", 1000.0),
                ConversionUnit("Kilocalories (kcal)", 4184.0),
                ConversionUnit("Watt-Hours (Wh)", 3600.0),
                ConversionUnit("Kilowatt-Hours (kWh)", 3.6e6)
            )
        ),
        ConversionCategory(
            title = "Power",
            units = listOf(
                ConversionUnit("Watts (W)", 1.0), // Base
                ConversionUnit("Calories/sec", 4.184),
                ConversionUnit("Horsepower (hp)", 745.699872),
                ConversionUnit("Kilowatts (kW)", 1000.0),
                ConversionUnit("Megawatts (MW)", 1e6)
            )
        ),
        ConversionCategory(
            title = "Pressure",
            units = listOf(
                ConversionUnit("Pascals (Pa)", 1.0), // Base
                ConversionUnit("Kilopascals (kPa)", 1000.0),
                ConversionUnit("Psi", 6894.75729),
                ConversionUnit("Bar", 100000.0),
                ConversionUnit("Atmosphere (atm)", 101325.0)
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
        val valueInBase = amount * from.factor
        return valueInBase / to.factor
    }
}

// Utility to format values dynamically, trimming redundant zeroes and using scientific notation when extremely small
fun formatValue(value: Double): String {
    if (value == 0.0) return "0"
    val absValue = Math.abs(value)
    val symbols = DecimalFormatSymbols(Locale.US)
    return if (absValue < 1e-6) {
        val df = DecimalFormat("0.######E0", symbols)
        df.format(value)
    } else {
        val df = DecimalFormat("#.########", symbols)
        val formatted = df.format(value)
        if (formatted.contains(".")) {
            formatted.trimEnd('0').trimEnd('.')
        } else {
            formatted
        }
    }
}
