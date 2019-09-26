package com.hotmail.leon.zimmermann.homeassistant.models.measure

enum class Measure(
    val text: String,
    val abbreviation: String,
    private val baseFactor: Double
) {
    MILLIGRAM("milligram", "mg", 0.001),
    GRAM("gram", "g", 1.0),
    KILOGRAM("kilogram", "kg", 1000.0),
    MILILITER("milliliter", "ml", 0.001),
    CENTILITER("centiliter", "cl", 0.01),
    DECILITER("deciliter", "dl", 0.1),
    LITER("liter", "l", 1.0);

    fun toBaseMeasure(value: Double) = value * baseFactor
    fun fromBaseMeasure(value: Double) = value / baseFactor
}