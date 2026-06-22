package com.example.unitconverter.data

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel

data class UnitCategoryState(
    val categoryTitle: String,
    val valueInput: String,
    val selectedFromUnit: ConversionUnit,
    val selectedToUnit: ConversionUnit
)

class UnitConverterViewModel : ViewModel() {
    private val _states = mutableStateMapOf<String, UnitCategoryState>()

    fun getCategoryState(category: ConversionCategory): UnitCategoryState {
        return _states.getOrPut(category.title) {
            UnitCategoryState(
                categoryTitle = category.title,
                valueInput = "1",
                selectedFromUnit = getDefaultFromUnit(category),
                selectedToUnit = getDefaultToUnit(category)
            )
        }
    }

    fun updateValueInput(categoryTitle: String, value: String) {
        val currentState = _states[categoryTitle] ?: return
        _states[categoryTitle] = currentState.copy(valueInput = value)
    }

    fun updateSelectedFromUnit(categoryTitle: String, unit: ConversionUnit) {
        val currentState = _states[categoryTitle] ?: return
        _states[categoryTitle] = currentState.copy(selectedFromUnit = unit)
    }

    fun updateSelectedToUnit(categoryTitle: String, unit: ConversionUnit) {
        val currentState = _states[categoryTitle] ?: return
        _states[categoryTitle] = currentState.copy(selectedToUnit = unit)
    }

    fun swapUnits(categoryTitle: String) {
        val currentState = _states[categoryTitle] ?: return
        _states[categoryTitle] = currentState.copy(
            selectedFromUnit = currentState.selectedToUnit,
            selectedToUnit = currentState.selectedFromUnit
        )
    }
}

private fun getDefaultFromUnit(category: ConversionCategory): ConversionUnit {
    val name = when (category.title) {
        "Length" -> "Kilometers"
        "Weight" -> "Kilograms"
        "Temperature" -> "Celsius"
        "Area" -> "Sq Meters"
        "Volume" -> "Liters"
        "Storage" -> "Gigabytes (GB)"
        "Time" -> "Hours"
        "Speed" -> "km/h"
        "Energy" -> "Kilocalories (kcal)"
        "Power" -> "Kilowatts (kW)"
        "Pressure" -> "Psi"
        else -> category.units[0].name
    }
    return category.units.firstOrNull { it.name == name } ?: category.units[0]
}

private fun getDefaultToUnit(category: ConversionCategory): ConversionUnit {
    val name = when (category.title) {
        "Length" -> "Meters"
        "Weight" -> "Grams"
        "Temperature" -> "Fahrenheit"
        "Area" -> "Sq Feet"
        "Volume" -> "Milliliters"
        "Storage" -> "Megabytes (MB)"
        "Time" -> "Minutes"
        "Speed" -> "mph"
        "Energy" -> "Kilojoules (kJ)"
        "Power" -> "Horsepower (hp)"
        "Pressure" -> "Bar"
        else -> category.units.getOrElse(1) { category.units[0] }.name
    }
    return category.units.firstOrNull { it.name == name } ?: category.units.getOrElse(1) { category.units[0] }
}
