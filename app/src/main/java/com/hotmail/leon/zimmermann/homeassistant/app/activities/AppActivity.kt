package com.hotmail.leon.zimmermann.homeassistant.app.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.hotmail.leon.zimmermann.homeassistant.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.app_activity.*
import kotlinx.android.synthetic.main.drawer_header.view.*

@AndroidEntryPoint
class AppActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.app_activity)
    initNavigation()
  }

  private fun initNavigation() {
    navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
    setupActionBarWithNavController(navController, appBarConfiguration)
    navigation_view.setupWithNavController(navController)
    navigation_view.setNavigationItemSelectedListener(this)
    initDrawerHeaderView(navigation_view.inflateHeaderView(R.layout.drawer_header))
  }

  private fun initDrawerHeaderView(view: View) {
    FirebaseAuth.getInstance().currentUser?.let { user ->
      user.photoUrl?.let {
        Glide.with(this)
          .load(it)
          .circleCrop()
          .into(view.profile_image)
      }
      user.displayName?.let { view.name_text.text = it }
      user.email?.let { view.email_text.text = it }
    }
  }

  override fun onNavigationItemSelected(item: MenuItem) = when (item.itemId) {
      R.id.shopping_option -> {
          navController.navigate(R.id.action_global_shopping_fragment)
          drawer.closeDrawer(navigation_view)
          true
      }
      R.id.planner_option -> {
          navController.navigate(R.id.action_global_planner_fragment)
          drawer.closeDrawer(navigation_view)
          true
      }
      R.id.management_option -> {
          navController.navigate(R.id.action_global_management)
          drawer.closeDrawer(navigation_view)
          true
      }
      R.id.settings_option -> {
          navController.navigate(R.id.action_global_settings_fragment)
          drawer.closeDrawer(navigation_view)
          true
      }
      R.id.about_option -> {
          navController.navigate(R.id.action_global_about_fragment)
          drawer.closeDrawer(navigation_view)
          true
      }
    else -> false
  }

  override fun onSupportNavigateUp(): Boolean {
    return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
  }
}