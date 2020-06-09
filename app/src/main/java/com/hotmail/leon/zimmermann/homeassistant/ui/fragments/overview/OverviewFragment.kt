package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview

import android.os.Bundle
import android.view.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Product
import com.hotmail.leon.zimmermann.homeassistant.ui.components.SimpleProductPreviewAdapter
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel
    private lateinit var authentication: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        authentication = Firebase.auth
        findNavController().popBackStack(R.id.splash_screen_fragment, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.overview_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        initializeDiscrepancyCard()
        initializeManagementCard()
        initializeCalendarCard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.account_option -> {
            findNavController().navigate(R.id.action_global_account_fragment)
            true
        }
        // TODO Add Settings
        else -> super.onOptionsItemSelected(item)
    }

    private fun initializeDiscrepancyCard() {
        val adapter = SimpleProductPreviewAdapter(context!!)
        overview_discrepancy_container.adapter = adapter
        overview_discrepancy_container.layoutManager = LinearLayoutManager(context!!)
        // TODO Init SimpleProductPreviewAdapter
        shopping_button.setOnClickListener {
            // TODO Add Navigation
        }
    }

    private fun initializeManagementCard() {
        management_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_meal_editor_fragment, bundleOf(
                "productId" to "nqPolU3YAE7FjreqYGwd"
            ))
        }
    }

    private fun initializeCalendarCard() {
        calendar_button.setOnClickListener {
            // TODO Add Navigation
        }
    }

}
