package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
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
        Toast.makeText(context, authentication.currentUser.toString(), Toast.LENGTH_LONG).show()
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
        R.id.settings_option -> {
            findNavController().navigate(R.id.action_global_settings_fragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initializeDiscrepancyCard() {
        val adapter = SimpleProductPreviewAdapter(context!!)
        overview_discrepancy_container.adapter = adapter
        overview_discrepancy_container.layoutManager = LinearLayoutManager(context!!)
        viewModel.productEntityList.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.productAmountList = it.map { product -> Pair(product, product.discrepancy) } }
        })
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_shopping_fragment)
        }
    }

    private fun initializeManagementCard() {
        management_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_management_fragment)
        }
    }

    private fun initializeCalendarCard() {
        calendar_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_calendar_fragment)
        }
    }

}
