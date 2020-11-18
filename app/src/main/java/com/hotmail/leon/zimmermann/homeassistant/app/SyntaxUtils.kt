package com.hotmail.leon.zimmermann.homeassistant.app

fun Boolean.orElse(function: () -> Unit) {
  if (!this) function()
}