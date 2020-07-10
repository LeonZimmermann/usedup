package com.hotmail.leon.zimmermann.homeassistant.datamodel.callbacks

import com.google.android.gms.tasks.Task

interface FirestoreCallback {
    fun onFirestoreResult(task: Task<*>)
    fun onValidationFailed(exception: Exception)
}