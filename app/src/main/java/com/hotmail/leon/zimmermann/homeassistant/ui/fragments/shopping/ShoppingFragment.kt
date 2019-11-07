package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_fragment.*

class ShoppingFragment : Fragment() {

    companion object {
        fun newInstance() = ShoppingFragment()
    }

    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shopping_fragment, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.shopping_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.submit_option -> {
            submitTransaction()
            true
        }
        else -> false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        adapter = ShoppingListAdapter(context!!)
        val layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(shopping_list.context, layoutManager.orientation)
        divider.setDrawable(context!!.getDrawable(R.drawable.divider)!!)
        shopping_list.addItemDecoration(divider)
        shopping_list.adapter = adapter
        shopping_list.layoutManager = layoutManager
        viewModel.productEntityList.observe(this, Observer { productList ->
            adapter.setProductList(productList)
        })
    }

    private fun submitTransaction() {
        viewModel.productEntityList.observe(this, Observer { productList ->
            val changedProducts = productList.filter { adapter.checkedProducts.contains(it.id) }
            changedProducts.forEach { it.quantity += it.discrepancy }
            viewModel.updateAll(changedProducts)
        })
    }
}
