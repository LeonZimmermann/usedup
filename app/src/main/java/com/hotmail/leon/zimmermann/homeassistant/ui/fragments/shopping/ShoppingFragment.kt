package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
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
        return inflater.inflate(R.menu.shopping_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        adapter = ShoppingListAdapter(context!!, {
            ItemTouchHelper(ShoppingListItemTouchHelperCallback()).apply {
                attachToRecyclerView(shopping_list)
            }
        }, { value, editCallback, deleteCallback ->
            ShoppingProductEditDialog(value, editCallback, deleteCallback).show(
                fragmentManager!!,
                "ShoppingProductEditDialog"
            )
        })
        initShoppingList()
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.submit_option -> {
            submitTransaction()
            findNavController().navigateUp()
            true
        }
        R.id.add_option -> {
            TODO("Implement")
            true
        }
        else -> false
    }

    private fun initShoppingList() {
        shopping_list.adapter = adapter
        shopping_list.layoutManager = LinearLayoutManager(context!!)
        viewModel.shoppingList.observe(this, Observer { shoppingList ->
            adapter.setShoppingList(shoppingList)
        })
        viewModel.categoryList.observe(this, Observer { categoryList ->
            adapter.setShoppingListOrder(categoryList)
        })
    }

    private fun submitTransaction() {
        viewModel.shoppingList.observe(this, Observer { shoppingList ->
            val changedProducts = shoppingList
                .toList()
                .map { it.second }
                .filter { it.isNotEmpty() }
                .reduce { list, acc -> (acc + list).toMutableList() }
                .filter { it.checked }
            changedProducts.forEach { it.product.quantity += it.product.discrepancy }
            viewModel.updateAll(changedProducts.map { it.product })
        })
    }
}
