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
                R.id.shopping_item -> {
                    nav_host_fragment.findNavController().navigate(R.id.action_stock_list)
                    true
                }
                R.id.management_item -> {
                    nav_host_fragment.findNavController().navigate(R.id.action_stock_management)
                    true
                }
                else -> false
            }
        }
    }


}
