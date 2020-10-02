package com.hotmail.leon.zimmermann.homeassistant.app.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.*
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        authenticate()
    }

    private fun authenticate() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            REQUEST_CODE_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SIGN_IN) handleSignInResult(resultCode, data)
    }

    private fun handleSignInResult(resultCode: Int, data: Intent?) {
        val response = IdpResponse.fromResultIntent(data)
        if (resultCode == Activity.RESULT_OK) {
            GlobalScope.launch(Dispatchers.IO) {
                initData()
            }.invokeOnCompletion {
                val intent = Intent(this@MainActivity, AppActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                }
                startActivity(intent)
            }
        } else response?.let { Toast.makeText(this, it.error?.message, Toast.LENGTH_LONG).show() }
    }

    private fun initData() {
        CategoryRepository.init()
        MeasureRepository.init()
    }

    companion object {
        private const val REQUEST_CODE_SIGN_IN = 0x01
    }
}
