package de.usedup.android.utils

import android.content.Context
import de.usedup.android.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.*

private val FLOATING_POINT_FORMAT = DecimalFormat("0.##")
fun Double.toFloatFormat(): String = FLOATING_POINT_FORMAT.format(this)

private val INT_FORMAT = DecimalFormat("0")
fun Int.toIntFormat(): String = INT_FORMAT.format(this)

fun DayOfWeek.toDisplayString(): String =
  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) getDisplayName(
    java.time.format.TextStyle.FULL, Locale.getDefault()) else name

fun Iterable<*>.enumerationJoin(context: Context): String {
  val separator = ", "
  val enumerationEnding = context.resources.getString(R.string.enumeration_ending)
  val string = this.joinToString(separator)
  val index = string.lastIndexOf(separator)
  return if (index != -1) string.replaceRange(index, index + separator.length, " $enumerationEnding ")
  else string
}

fun Int.toDurationString() = "$this min"

fun Long.toLocalDate(): LocalDate = Instant.ofEpochSecond(this).atZone(ZoneId.systemDefault()).toLocalDate()
fun LocalDate.toLongValue() = this.atStartOfDay(ZoneId.systemDefault()).toEpochSecond()

private val TIME_FORMAT = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)
fun Calendar.toTimeDisplayString(): String = TIME_FORMAT.format(time)