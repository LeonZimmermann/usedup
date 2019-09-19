package com.hotmail.leon.zimmermann.homeassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initializeBottomNavigation()
    }

    private fun initializeBottomNavigation() {
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.shopping_option -> {
                    nav_host_fragment.findNavController().navigate(R.id.action_global_shopping_fragment)
                    true
                }
                R.id.management_option -> {
                    nav_host_fragment.findNavController().navigate(R.id.action_global_management_fragment)
                    true
                }
                R.id.transaction_option -> {
                    nav_host_fragment.findNavController().navigate(R.id.action_global_transaction_fragment)
                    true
                }
                else -> false

            }
        }
    }


}
