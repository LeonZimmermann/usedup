package com.hotmail.leon.zimmermann.homeassistant.ui.management

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.management.dialog.ManagementItemDialogAddHandler
import com.hotmail.leon.zimmermann.homeassistant.ui.management.dialog.ManagementItemDialogEditHandler
import com.hotmail.leon.zimmermann.homeassistant.ui.management.dialog.ManagementItemDialogFragment
import kotlinx.android.synthetic.main.management_fragment.*


class ManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ManagementFragment()
    }

    private lateinit var viewModel: ManagementViewModel
    private lateinit var adapter: ManagementListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.management_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManagementViewModel::class.java)
        initializeProductList()
        initializeAddButton()
    }

    private fun initializeProductList() {
        val layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(product_entry_container.context, layoutManager.orientation)
        divider.setDrawable(context!!.getDrawable(R.drawable.divider)!!)
        product_entry_container.addItemDecoration(divider)
        adapter = ManagementListAdapter(
            context!!,
            View.OnClickListener { onItemClicked(it) })
        product_entry_container.adapter = adapter
        product_entry_container.layoutManager = layoutManager
        viewModel.productEntityList.observe(this, Observer {
            it?.let { adapter.setProductList(it) }
        })
    }

    private fun initializeAddButton() {
        add_button.setOnClickListener {
            ManagementItemDialogFragment(
                ManagementItemDialogAddHandler,
                null
            )
                .show(fragmentManager!!, "ManagementItemDialogFragment")
        }
    }

    private fun onItemClicked(view: View) {
        val productId = adapter[product_entry_container.getChildAdapterPosition(view)].id
        ManagementItemDialogFragment(
            ManagementItemDialogEditHandler,
            productId
        ).show(fragmentManager!!, "ManagementItemDialogFragment")
    }
}
