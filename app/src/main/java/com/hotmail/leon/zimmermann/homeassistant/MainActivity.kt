package com.hotmail.leon.zimmermann.homeassistant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.product_entry.view.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.list_item -> {
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
