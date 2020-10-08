package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions.DataIntegrityException
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseMeasure
import junit.framework.Assert.assertEquals
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class MeasureTest {
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
            Measure("id", "name", "abbreviation", 1f, "type"),
            Measure.createInstance("id", FirebaseMeasure("name", "abbreviation", 1f, "type"))
        )
    }

    @Test
    fun testToBase() {
        val measure = Measure("id", "Measure", "m", 0.5f, "type")
        assertEquals(2.0, 4.0.toBase(measure))
    }

    @Test
    fun testFromBase() {
        val measure = Measure("id", "Measure", "m", 0.5f, "type")
        assertEquals(8.0, 4.0.toMeasure(measure))
    }
}