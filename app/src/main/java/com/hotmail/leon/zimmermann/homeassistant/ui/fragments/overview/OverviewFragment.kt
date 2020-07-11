package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.overview

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.ui.components.SimpleProductPreviewAdapter
import kotlinx.android.synthetic.main.overview_fragment.*

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        initViewModel()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDiscrepancyCard()
        initManagementCard()
        initCalendarCard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.account_option -> {
            findNavController().navigate(R.id.action_global_account_fragment)
            true
        }
        // TODO Add Settings
        R.id.about_option -> {
            findNavController().navigate(R.id.action_global_about_fragment)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    private fun initDiscrepancyCard() {
        val adapter = SimpleProductPreviewAdapter(context!!, discrepancy_recyclerview)
        discrepancy_recyclerview.adapter = adapter
        discrepancy_recyclerview.layoutManager = LinearLayoutManager(context!!)
        // TODO Move this to ViewModel (Maybe use RxJava)
        Firebase.firestore.collection(Product.COLLECTION_NAME).whereGreaterThan("discrepancy", 0).get()
            .addOnSuccessListener { documents ->
                adapter.productAmountList = documents.map { it.toObject<Product>() }
                    .map { Pair(it, it.discrepancy) }
            }
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_shopping_fragment)
        }
    }

    private fun initManagementCard() {
        management_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_management_fragment)
        }
    }

    private fun initCalendarCard() {
        calendar_button.setOnClickListener {
            findNavController().navigate(R.id.action_overview_fragment_to_timeline_fragment)
        }
    }

}
