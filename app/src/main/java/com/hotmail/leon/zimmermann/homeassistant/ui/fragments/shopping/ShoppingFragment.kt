package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.ItemKeyProvider
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
            findNavController().navigateUp()
            true
        }
        R.id.edit_option -> {
            findNavController().navigate(R.id.action_shopping_fragment_to_shopping_edit_fragment)
            true
        }
        else -> false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        adapter = ShoppingListAdapter(context!!)
        initShoppingList()
    }

    private fun initShoppingList() {
        val layoutManager = LinearLayoutManager(context!!)
        val divider = DividerItemDecoration(shopping_list.context, layoutManager.orientation)
        divider.setDrawable(context!!.getDrawable(R.drawable.divider)!!)
        shopping_list.addItemDecoration(divider)
        shopping_list.adapter = adapter
        shopping_list.layoutManager = layoutManager
        viewModel.shoppingList.observe(this, Observer { shoppingList ->
            adapter.setShoppingList(shoppingList)
        })
    }

    private fun submitTransaction() {
        viewModel.shoppingList.observe(this, Observer { shoppingList ->
            val changedProducts = shoppingList.filter { adapter.checkedEntries.contains(it.product.id) }
            changedProducts.forEach { it.product.quantity += it.amount }
            viewModel.updateAll(changedProducts.map { it.product })
        })
    }
}
