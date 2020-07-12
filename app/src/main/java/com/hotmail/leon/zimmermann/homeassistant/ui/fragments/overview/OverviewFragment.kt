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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDiscrepancyCard()
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    private fun initDiscrepancyCard() {
        val adapter = SimpleProductPreviewAdapter(context!!, discrepancy_recyclerview)
        discrepancy_recyclerview.adapter = adapter
        discrepancy_recyclerview.layoutManager = LinearLayoutManager(context!!)
        adapter.productAmountList = viewModel.products.map { Pair(it, it.discrepancy) }
        shopping_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_shopping_fragment)
        }
    }
}
