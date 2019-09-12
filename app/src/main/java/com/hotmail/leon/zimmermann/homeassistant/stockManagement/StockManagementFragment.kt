package com.hotmail.leon.zimmermann.homeassistant.stockManagement

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.stockManagement.itemDialog.StockItemDialog
import com.hotmail.leon.zimmermann.homeassistant.stockManagement.itemDialog.StockItemDialogAddMode
import com.hotmail.leon.zimmermann.homeassistant.stockManagement.itemDialog.StockItemDialogEditMode
import kotlinx.android.synthetic.main.product_entry.view.*
import kotlinx.android.synthetic.main.stock_management_fragment.*


class StockManagementFragment : Fragment() {
    companion object {
        fun newInstance() = StockManagementFragment()
    }

    private lateinit var viewModel: StockManagementViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stock_management_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StockManagementViewModel::class.java)
        initializeProductList()
        initializeAddButton()
    }

    private fun initializeProductList() {
        viewModel.productList.observe(this, Observer {
            product_entry_container.removeAllViews()
            it.forEach { product ->
                val view = layoutInflater.inflate(R.layout.product_entry, null)
                view.product_name.text = product.name
                view.product_quantity.text = product.current.toString()
                view.setOnClickListener {
                    StockItemDialog(StockItemDialogEditMode(viewModel.productList, product)).show(
                        fragmentManager!!,
                        "StockItemDialog"
                    )
                }
                product_entry_container.addView(view)
            }
        })
    }

    private fun initializeAddButton() {
        viewModel.productList.observe(this, Observer {
            add_button.setOnClickListener {
                StockItemDialog(StockItemDialogAddMode(viewModel.productList)).show(
                    fragmentManager!!,
                    "StockItemDialog"
                )
            }
        })
    }
}
