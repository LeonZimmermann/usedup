package de.usedup.android.datamodel.api.exceptions

class IncompatibleMeasuresException @JvmOverloads constructor(
    message: String? = null,
    cause: Throwable? = null,
    enableSuppression: Boolean = false,
    writableStackTrace: Boolean = true
) : Exception(message, cause, enableSuppression, writableStackTrace)