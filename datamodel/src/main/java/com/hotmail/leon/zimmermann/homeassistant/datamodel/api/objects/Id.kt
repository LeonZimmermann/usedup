package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects

import java.io.Serializable

interface Id: Serializable {
  override fun equals(other: Any?): Boolean
}