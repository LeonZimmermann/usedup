package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.creation

open class ConsumptionException(message: String) : Exception(message)
class InvalidQuantityChangeException : ConsumptionException("Invalid Quantity Change")
class InvalidProductNameException : ConsumptionException("Invalid ProductEntity Name")
class NoConsumptionsException : ConsumptionException("No consumptions specified")