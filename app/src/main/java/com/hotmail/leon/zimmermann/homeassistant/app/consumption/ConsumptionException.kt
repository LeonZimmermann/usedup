package com.hotmail.leon.zimmermann.homeassistant.app.consumption

import kotlin.Exception

open class ConsumptionException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    enableSuppression: Boolean = false,
    writableStackTrace: Boolean = true
) : Exception(message, cause, enableSuppression, writableStackTrace)