package com.hotmail.leon.zimmermann.homeassistant.management

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
import com.hotmail.leon.zimmermann.homeassistant.management.dialog.ManagementItemDialog
import com.hotmail.leon.zimmermann.homeassistant.management.dialog.ProductItemDialogAddMode
import com.hotmail.leon.zimmermann.homeassistant.management.dialog.ProductItemDialogEditMode
import kotlinx.android.synthetic.main.management_fragment.*
import kotlinx.android.synthetic.main.shopping_fragment.*


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
        adapter = ManagementListAdapter(context!!, View.OnClickListener { onItemClicked(it) })
        product_entry_container.adapter = adapter
        product_entry_container.layoutManager = layoutManager
        viewModel.productList.observe(this, Observer {
            it?.let { adapter.setProductList(it) }
        })
    }

    private fun initializeAddButton() {
        add_button.setOnClickListener {
            ManagementItemDialog(ProductItemDialogAddMode(viewModel::insert))
                .show(fragmentManager!!, "ManagementItemDialog")
        }
    }

    private fun onItemClicked(view: View) {
        ManagementItemDialog(
            ProductItemDialogEditMode(
                adapter[product_entry_container.getChildAdapterPosition(view)],
                viewModel::update,
                viewModel::delete
            )
        ).show(fragmentManager!!, "ManagementItemDialog")
    }
}
