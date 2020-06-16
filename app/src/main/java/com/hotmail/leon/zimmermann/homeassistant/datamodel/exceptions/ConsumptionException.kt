package com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions

import kotlin.Exception

open class ConsumptionException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    enableSuppression: Boolean = false,
    writableStackTrace: Boolean = false
) : Exception(message, cause, enableSuppression, writableStackTrace)