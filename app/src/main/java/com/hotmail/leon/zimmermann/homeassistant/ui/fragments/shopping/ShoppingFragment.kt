package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.ProductRepository
import kotlinx.android.synthetic.main.shopping_fragment.*

class ShoppingFragment : Fragment() {
    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.shopping_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
        initShoppingList()
        initConfirmButton()
    }

    private fun initViewModel() {
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    private fun initAdapter() {
        adapter = ShoppingListAdapter(context!!)
        adapter.setData(
            CategoryRepository.categories,
            ProductRepository.products
                .filter { it.discrepancy > 0 }
                .map { ShoppingProduct(it) }
                .groupBy { CategoryRepository.getCategoryForId(it.product.category!!.id) })
    }

    private fun initShoppingList() {
        shopping_list.adapter = adapter
        shopping_list.layoutManager = LinearLayoutManager(context!!)
    }

    private fun initConfirmButton() {
        confirm_button.setOnClickListener {
            submitTransaction()
            findNavController().navigateUp()
        }
    }

    private fun submitTransaction() {
        /*
        viewModel.shoppingList.observe(viewLifecycleOwner, Observer { shoppingList ->
            val changedProducts = shoppingList
                .toList()
                .map { it.second }
                .filter { it.isNotEmpty() }
                .reduce { list, acc -> (acc + list).toMutableList() }
                .filter { it.checked }
            changedProducts.forEach { it.product.quantity += it.product.discrepancy }
            viewModel.updateAll(changedProducts.map { it.product })
        })
        */
    }

    companion object {
        fun newInstance() = ShoppingFragment()
    }
}
