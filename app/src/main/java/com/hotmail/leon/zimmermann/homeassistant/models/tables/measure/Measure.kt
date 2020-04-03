package com.hotmail.leon.zimmermann.homeassistant.models.tables.measure

enum class Measure(
    val text: String,
    val abbreviation: String,
    private val type: MeasureType,
    private val baseFactor: Double
) {
    GRAM("gram", "g", MeasureType.WEIGHT, 1.0),
    MILLIGRAM("milligram", "mg", MeasureType.WEIGHT, 0.001),
    KILOGRAM("kilogram", "kg", MeasureType.WEIGHT, 1000.0),
    MILILITER("milliliter", "ml", MeasureType.VOLUME, 0.001),
    CENTILITER("centiliter", "cl", MeasureType.VOLUME, 0.01),
    DECILITER("deciliter", "dl", MeasureType.VOLUME, 0.1),
    LITER("liter", "l", MeasureType.VOLUME, 1.0);

    val id: Long = ordinal.toLong()

    fun toBaseMeasure(value: Double) = value * baseFactor
    fun fromBaseMeasure(value: Double) = value / baseFactor
    @Throws(MeasureConversionException::class)
    fun toMeasure(value: Double, measure: Measure): Double {
        if (measure.type != type) throw MeasureConversionException(type, measure.type)
        return measure.fromBaseMeasure(toBaseMeasure(value))
    }

}

enum class MeasureType {
    WEIGHT,
    VOLUME;
}

class MeasureConversionException(from: MeasureType, to: MeasureType):
    Exception("Cannot convert from ${from.name} to ${to.name}")