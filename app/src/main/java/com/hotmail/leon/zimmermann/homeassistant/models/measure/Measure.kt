package com.hotmail.leon.zimmermann.homeassistant.models.measure

enum class Measure(
    val text: String,
    val abbreviation: String
) {
    MILIGRAM("milligram", "mg"),
    GRAM("gram", "g"),
    KILOGRAM("kilogram", "kg"),
    MILILITER("milliliter", "ml"),
    CENTILITER("centiliter", "cl"),
    DECILITER("deciliter", "dl"),
    LITER("liter", "l")
}