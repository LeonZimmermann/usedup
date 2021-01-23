package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingList
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shopping_fragment.*

@AndroidEntryPoint
class ShoppingFragment : Fragment() {

  private val viewModel: ShoppingViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireNotNull(arguments).apply {
      viewModel.initShoppingList(
        getSerializable(SHOPPING_LIST) as? ShoppingList ?: throw RuntimeException("No shopping-list provided"))
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.shopping_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initShoppingListRecyclerView()
  }

  private fun initShoppingListRecyclerView() {
    val adapter = ShoppingListCategoryRecyclerAdapter(requireContext())
    shopping_list_recycler_view.adapter = adapter
    shopping_list_recycler_view.layoutManager = LinearLayoutManager(requireContext())
    viewModel.shoppingListCategories.observe(viewLifecycleOwner, Observer { shoppingListCategories ->
      adapter.initShoppingListCategories(shoppingListCategories)
    })
  }

  companion object {
    const val SHOPPING_LIST = "shoppingList"

    fun newInstance() = ShoppingFragment()
  }
}
