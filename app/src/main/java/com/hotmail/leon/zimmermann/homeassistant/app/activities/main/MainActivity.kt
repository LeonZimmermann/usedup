package com.hotmail.leon.zimmermann.homeassistant.app.activities.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.hotmail.leon.zimmermann.homeassistant.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val viewModel: MainActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity)
    initActivityToStart()
    handleLoginStatus()
  }

  private fun initActivityToStart() {
    viewModel.activityToStart.observe(this, Observer { activityToStart ->
      val intent = Intent(this@MainActivity, activityToStart).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
      startActivity(intent)
    })
  }

  private fun handleLoginStatus() {
    if (FirebaseAuth.getInstance().currentUser != null) {
      viewModel.initAppAndStartActivity()
    } else {
      viewModel.initLoginFlow()
    }
  }

}
