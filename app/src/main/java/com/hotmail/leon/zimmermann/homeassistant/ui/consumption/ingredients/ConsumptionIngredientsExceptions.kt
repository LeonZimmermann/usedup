package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.ingredients

open class ConsumptionIngredientsException(message: String) : Exception(message)
class InvalidQuantityChangeException : ConsumptionIngredientsException("Invalid Quantity Change")
class InvalidProductNameException : ConsumptionIngredientsException("Invalid ProductEntity Name")
class NoConsumptionsException : ConsumptionIngredientsException("No consumptions specified")