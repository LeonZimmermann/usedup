package de.usedup.android.activities.loading

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R

@AndroidEntryPoint
class LoadingActivity : AppCompatActivity() {

  private val viewModel: LoadingActivityViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.loading_activity)
    initActivityToStart()
    viewModel.initializeApp()
  }

  private fun initActivityToStart() {
    viewModel.activityToStart.observe(this, { activityToStart ->
      val intent = Intent(this@LoadingActivity, activityToStart).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
      startActivity(intent)
    })
  }
}