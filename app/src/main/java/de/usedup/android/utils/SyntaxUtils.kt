package de.usedup.android.utils

fun Boolean.orElse(function: () -> Unit) {
  if (!this) function()
}