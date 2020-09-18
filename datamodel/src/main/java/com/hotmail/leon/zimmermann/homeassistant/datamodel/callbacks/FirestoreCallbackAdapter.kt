package com.hotmail.leon.zimmermann.homeassistant.datamodel.callbacks

import com.google.android.gms.tasks.Task

class FirestoreCallbackAdapter: FirestoreCallback {
    override fun onFirestoreResult(task: Task<*>) {}
    override fun onValidationFailed(exception: Exception) {}
}