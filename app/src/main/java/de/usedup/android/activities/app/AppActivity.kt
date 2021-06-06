package de.usedup.android.activities.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import kotlinx.android.synthetic.main.app_activity.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.URL
import javax.net.ssl.HttpsURLConnection


@AndroidEntryPoint
class AppActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

  private val viewModel: AppActivityViewModel by viewModels()

  private lateinit var navController: NavController
  private lateinit var appBarConfiguration: AppBarConfiguration
  private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

  private lateinit var connectivityManager: ConnectivityManager

  private val networkCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
    override fun onAvailable(network: Network) {
      updateInternetConnectionStatus()
    }

    override fun onLost(network: Network) {
      updateInternetConnectionStatus()
    }
  }

  private val wifiStatusReceiver: BroadcastReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      updateInternetConnectionStatus()
    }
  }

  fun updateInternetConnectionStatus() = lifecycleScope.launch(Dispatchers.IO) {
    try {
      val googleConnection: HttpsURLConnection = URL("https://www.google.com").openConnection() as HttpsURLConnection
      googleConnection.setRequestProperty("User-Agent", "Test")
      googleConnection.setRequestProperty("Connection", "close")
      googleConnection.connectTimeout = 1500
      googleConnection.connect()
      viewModel.setConnectionState(googleConnection.responseCode == 200)
    } catch (e: IOException) {
      Log.e(TAG, "Error checking internet connection", e)
      viewModel.setConnectionState(false)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.app_activity)
    setSupportActionBar(toolbar)
    initNavigation()
    connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
    connectivityManager.registerDefaultNetworkCallback(networkCallback)
    registerReceiver(wifiStatusReceiver, IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION))
    viewModel.setConnectionState((getSystemService(WIFI_SERVICE) as WifiManager).isWifiEnabled)
  }

  override fun onPause() {
    super.onPause()
    viewModel.saveNavigationState(navController)
  }

  override fun onResume() {
    super.onResume()
    viewModel.restoreNavigationState(navController)
  }

  override fun onDestroy() {
    super.onDestroy()
    connectivityManager.unregisterNetworkCallback(networkCallback)
    unregisterReceiver(wifiStatusReceiver);
  }

  private fun initNavigation() {
    navController = findNavController(R.id.nav_host_fragment)
    appBarConfiguration = AppBarConfiguration(navController.graph, drawer)
    actionBarDrawerToggle = ActionBarDrawerToggle(this, drawer, 0, 0)
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
    R.id.household_option -> {
      navController.navigate(R.id.action_global_household_fragment)
      drawer.closeDrawer(navigation_view)
      true
    }
    R.id.management_option -> {
      navController.navigate(R.id.action_global_management)
      drawer.closeDrawer(navigation_view)
      true
    }
    R.id.statistics_option -> {
      Snackbar.make(container, getString(R.string.coming_soon), Snackbar.LENGTH_LONG)
        .setAction(R.string.read_more) {
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://usedup.de")))
        }
        .show()
      drawer.closeDrawer(navigation_view)
      true
    }
    R.id.history_option -> {
      Snackbar.make(container, getString(R.string.coming_soon), Snackbar.LENGTH_LONG)
        .setAction(R.string.read_more) {
          startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://usedup.de")))
        }
        .show()
      drawer.closeDrawer(navigation_view)
      true
    }
    R.id.account_option -> {
      navController.navigate(R.id.action_global_account_fragment)
      drawer.closeDrawer(navigation_view)
      true
    }
    R.id.preference_option -> {
      navController.navigate(R.id.action_global_preference_fragment)
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

  companion object {
    const val TAG = "AppActivity"
  }
}