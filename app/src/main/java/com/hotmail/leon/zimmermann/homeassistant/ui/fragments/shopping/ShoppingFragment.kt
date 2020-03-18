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
import org.jetbrains.anko.support.v4.toast

// TODO Input-Error-Handling needs to be reworked. Instead of calling toast() immediately throw exception and handle error separately
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
        shopping_confirm_cart_button.setOnClickListener {
            submitTransaction()
            findNavController().navigateUp()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.add_option -> {
            val productList = viewModel.productList.value!!
            ShoppingProductAddDialog(productList.map { it.name }) { name, cartAmount ->
                val product = productList.first { it.name == name }
                val shoppingProduct = ShoppingProduct(product, cartAmount)
                val shoppingList = viewModel.shoppingList.value!!
                shoppingList.let {
                    if (it.containsKey(product.categoryId)) {
                        if (it[product.categoryId]!!.firstOrNull { shoppingProductInList ->
                                shoppingProductInList.product.id == shoppingProduct.product.id
                            } != null)
                            toast("Product already exists")
                        else it[product.categoryId]!!.add(shoppingProduct)
                    } else it[product.categoryId] = mutableListOf(shoppingProduct)
                }
                viewModel.shoppingList.value = shoppingList
            }.show(fragmentManager!!, "ShoppingProductAddDialog")
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
            adapter.setCategories(categoryList)
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
