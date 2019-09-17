package com.hotmail.leon.zimmermann.homeassistant.management

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.management.dialog.ProductItemDialog
import com.hotmail.leon.zimmermann.homeassistant.management.dialog.ProductItemDialogAddMode
import com.hotmail.leon.zimmermann.homeassistant.management.dialog.ProductItemDialogEditMode
import kotlinx.android.synthetic.main.product_management_fragment.*


class ProductManagementFragment : Fragment() {
    companion object {
        fun newInstance() = ProductManagementFragment()
    }

    private lateinit var viewModel: ProductManagementViewModel
    private lateinit var adapter: ProductListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_management_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProductManagementViewModel::class.java)
        initializeProductList()
        initializeAddButton()
    }

    private fun initializeProductList() {
        adapter = ProductListAdapter(activity!!, View.OnClickListener { onItemClicked(it) })
        product_entry_container.adapter = adapter
        product_entry_container.layoutManager = LinearLayoutManager(activity)
        viewModel.productList.observe(this, Observer {
            it?.let { adapter.setProductList(it) }
        })
    }

    private fun initializeAddButton() {
        add_button.setOnClickListener {
            ProductItemDialog(ProductItemDialogAddMode(viewModel::insert))
                .show(fragmentManager!!, "ProductItemDialog")
        }
    }

    private fun onItemClicked(view: View) {
        ProductItemDialog(ProductItemDialogEditMode(adapter[product_entry_container.getChildAdapterPosition(view)],
            { old, new ->
                viewModel.delete(old)
                viewModel.insert(new)
            }, {
                viewModel.delete(it)
            })
        ).show(fragmentManager!!, "ProductItemDialog")
    }
}
