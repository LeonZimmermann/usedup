package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseMeasure
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class MeasureUnitTest {
    @Test
    fun testCreateInstance() {
        assertThatThrownBy {
            Measure.createInstance("", FirebaseMeasure(null, "abbreviation", 1f, "type"))
        }.isInstanceOf(DataIntegrityException::class.java)
        assertThatThrownBy {
            Measure.createInstance("", FirebaseMeasure("name", null, 1f, "type"))
        }.isInstanceOf(DataIntegrityException::class.java)
        assertThatThrownBy {
            Measure.createInstance("", FirebaseMeasure("name", "abbreviation", null, "type"))
        }.isInstanceOf(DataIntegrityException::class.java)
        assertThatThrownBy {
            Measure.createInstance("", FirebaseMeasure("name", "abbreviation", 1f, null))
        }.isInstanceOf(DataIntegrityException::class.java)
        assertEquals(
            Measure(mockk(), "name", "abbreviation", 1f, "type"),
            Measure.createInstance(mockk(), FirebaseMeasure("name", "abbreviation", 1f, "type"))
        )
    }

    @Test
    fun testToBase() {
        val measure = Measure(mockk(), "Measure", "m", 0.5f, "type")
        assertEquals(2.0, 4.0.toBase(measure))
    }

    @Test
    fun testFromBase() {
        val measure = Measure(mockk(), "Measure", "m", 0.5f, "type")
        assertEquals(8.0, 4.0.toMeasure(measure))
    }
}