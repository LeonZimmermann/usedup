package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.exceptions

import kotlin.Exception

class InvalidInputException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    enableSuppression: Boolean = false,
    writableStackTrace: Boolean = true
) : Exception(message, cause, enableSuppression, writableStackTrace)