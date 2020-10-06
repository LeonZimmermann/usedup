package com.hotmail.leon.zimmermann.homeassistant.app

import android.content.Context
import com.hotmail.leon.zimmermann.homeassistant.R
import java.text.DecimalFormat
import java.time.DayOfWeek

private val FLOATING_POINT_FORMAT = DecimalFormat("0.0#")
fun Double.toFloatFormat(): String = FLOATING_POINT_FORMAT.format(this)

private val INT_FORMAT = DecimalFormat("0")
fun Int.toIntFormat(): String = INT_FORMAT.format(this)

fun DayOfWeek.toDisplayString(): String =
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) getDisplayName(
    java.time.format.TextStyle.FULL, java.util.Locale.getDefault()) else name

fun Iterable<*>.enumerationJoin(context: Context): String {
  val separator = ", "
  val enumerationEnding = context.resources.getString(R.string.enumeration_ending)
  val string = this.joinToString(separator)
  val index = string.lastIndexOf(separator)
  return string.replaceRange(index, index + separator.length, enumerationEnding)
}