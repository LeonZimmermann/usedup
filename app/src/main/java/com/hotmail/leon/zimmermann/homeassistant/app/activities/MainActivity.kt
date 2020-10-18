package com.hotmail.leon.zimmermann.homeassistant.app.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.hotmail.leon.zimmermann.homeassistant.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    initActivityToStart()
    findViewById<Button>(R.id.login_button)
      .setOnClickListener { dispatchAuthenticationRequest() }
  }

  private fun initActivityToStart() {
    viewModel.activityToStart.observe(this, Observer { activityToStart ->
      val intent = Intent(this@MainActivity, activityToStart).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
      startActivity(intent)
    })
  }

  private fun dispatchAuthenticationRequest() {
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
    if (resultCode == Activity.RESULT_OK) viewModel.initAppAndStartActivity()
    else response?.let { Toast.makeText(this, it.error?.message, Toast.LENGTH_LONG).show() }
  }

  companion object {
    private const val REQUEST_CODE_SIGN_IN = 0x01
  }
}
