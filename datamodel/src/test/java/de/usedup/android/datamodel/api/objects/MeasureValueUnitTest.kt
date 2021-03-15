package de.usedup.android.datamodel.api.objects

import de.usedup.android.datamodel.api.exceptions.IncompatibleMeasuresException
import de.usedup.android.datamodel.firebase.objects.FirebaseMeasure
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class MeasureValueUnitTest {

    @Test
    fun testPlusException() {
        val measure = Measure.createInstance("", FirebaseMeasure("measure", "ms", 1f, "type"))
        val incompatibleMeasure = Measure.createInstance("", FirebaseMeasure("incompatibleMeasure", "cp", 1f, "otherType"))
        val measureValue = MeasureValue(5.0, measure)
        val otherMeasureValue = MeasureValue(5.0, incompatibleMeasure)
        assertThatThrownBy { measureValue + otherMeasureValue }.isInstanceOf(IncompatibleMeasuresException::class.java)
    }

    @Test
    fun testPlus() {
        val measure = Measure.createInstance("", FirebaseMeasure("measure", "ms", 1f, "type"))
        val compatibleMeasure = Measure.createInstance("", FirebaseMeasure("compatibleMeasure", "cp", .5f, "type"))
        val measureValue = MeasureValue(5.0, measure)
        val otherMeasureValue = MeasureValue(3.0, compatibleMeasure)
        val result = measureValue + otherMeasureValue
        assertEquals(measure, result.measure)
        assertEquals(6.5, result.double)
    }

    @Test
    fun testMinusException() {
        val measure = Measure.createInstance("", FirebaseMeasure("measure", "ms", 1f, "type"))
        val incompatibleMeasure = Measure.createInstance("", FirebaseMeasure("incompatibleMeasure", "cp", 1f, "otherType"))
        val measureValue = MeasureValue(5.0, measure)
        val otherMeasureValue = MeasureValue(5.0, incompatibleMeasure)
        assertThatThrownBy { measureValue - otherMeasureValue }.isInstanceOf(IncompatibleMeasuresException::class.java)
    }

    @Test
    fun testMinus() {
        val measure = Measure.createInstance("", FirebaseMeasure("measure", "ms", 1f, "type"))
        val compatibleMeasure = Measure.createInstance("", FirebaseMeasure("compatibleMeasure", "cp", .5f, "type"))
        val measureValue = MeasureValue(5.0, measure)
        val otherMeasureValue = MeasureValue(3.0, compatibleMeasure)
        val result = measureValue - otherMeasureValue
        assertEquals(measure, result.measure)
        assertEquals(3.5, result.double)
    }

    @Test
    fun testToString() {
        val measure = Measure.createInstance("", FirebaseMeasure("measure", "ms", 1f, "type"))
        val measureValue = MeasureValue(5.0, measure)
        assertEquals("5.0ms", measureValue.toString())
    }
}