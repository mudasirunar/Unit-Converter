# Unit & Currency Converter App

This is a simple Android utility application built using Jetpack Compose that provides real-time currency conversions and a comprehensive suite of unit converters.

## Key Features

### 1. Live Currency Exchange
- **Real-Time Data**: Integrates live exchange rates for over 160+ currencies using the [ExchangeRate-API](https://www.exchangerate-api.com/) service via Retrofit.
- **Offline Support**: Caches exchange rates locally in a Room database to support conversions when offline.
- **Quick Swap**: Easy currency swap action to toggle between source and target currencies.

### 2. Unit Converter Section
Supports 11 conversion categories:
- **Length**: Nanometers, Micrometers, Millimeters, Centimeters, Inches, Feet, Yards, Meters, Kilometers, Miles.
- **Weight**: Milligrams, Grams, Ounces, Pounds, Kilograms, Stones, Metric Tons.
- **Temperature**: Celsius, Fahrenheit, Kelvin.
- **Area**: Sq Inches, Sq Feet, Sq Yards, Sq Meters, Acres, Hectares, Sq Kilometers, Sq Miles.
- **Volume**: Milliliters, Fluid Ounces (US), Cups (US), Liters, Gallons (US), Cubic Feet, Cubic Meters.
- **Storage**: Bits, Bytes, Kilobits, Kilobytes, Megabits, Megabytes, Gigabits, Gigabytes, Terabits, Terabytes, Petabytes.
- **Time**: Nanoseconds, Microseconds, Milliseconds, Seconds, Minutes, Hours, Days, Weeks, Months, Years.
- **Speed**: Meters per second, Kilometers per hour, Miles per hour, Knots.
- **Energy**: Electronvolts, Joules, Calories, Kilojoules, Kilocalories, Watt-Hours, Kilowatt-Hours.
- **Power**: Watts, Calories/sec, Horsepower, Kilowatts, Megawatts.
- **Pressure**: Pascals, Kilopascals, Psi, Bar, Atmospheres.

## Technical Stack & Libraries
- **Kotlin**: Core programming language.
- **Jetpack Compose**: Declarative UI layout framework.
- **Room Database**: Local SQL caching layer.
- **Retrofit & Gson**: REST API client and serialization.
- **Lifecycle ViewModel**: Architecture states management.

## API Integrations
The currency converter calls endpoints hosted by:
- **ExchangeRate-API**  
  - Website: [https://www.exchangerate-api.com/](https://www.exchangerate-api.com/)
  - Base URL: `https://v6.exchangerate-api.com/`
