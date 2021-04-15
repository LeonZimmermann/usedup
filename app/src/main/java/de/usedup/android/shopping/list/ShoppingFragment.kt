package de.usedup.android.shopping.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.databinding.ShoppingFragmentBinding
import de.usedup.android.shopping.data.ShoppingCart
import de.usedup.android.shopping.data.ShoppingList
import kotlinx.android.synthetic.main.overview_fragment.*
import kotlinx.android.synthetic.main.shopping_fragment.*
import org.jetbrains.anko.sdk27.coroutines.onScrollChange


@AndroidEntryPoint
class ShoppingFragment : Fragment() {

  private val viewModel: ShoppingViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    requireNotNull(arguments).apply {
      (getSerializable(SHOPPING_CART) as? ShoppingCart)?.let { viewModel.restoreShoppingCart(it) }
      viewModel.initShoppingList(
        getSerializable(SHOPPING_LIST) as? ShoppingList ?: throw RuntimeException("No shopping-list provided"))
    }
  }

  override fun onDestroy() {
    viewModel.saveShoppingListState()
    super.onDestroy()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.shopping_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initShoppingListRecyclerView()
    initActionButtonVisibilityHandler()
  }

  private fun initDatabinding() {
    val binding = ShoppingFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  private fun initShoppingListRecyclerView() {
    val adapter = ShoppingListCategoryRecyclerAdapter(requireContext(), viewModel.onCheckButtonPressedCallback)
    shopping_list_recycler_view.adapter = adapter
    shopping_list_recycler_view.layoutManager = object : LinearLayoutManager(requireContext()) {
      override fun canScrollVertically(): Boolean = false
    }
    viewModel.shoppingListCategories.observe(viewLifecycleOwner, { shoppingListCategories ->
      adapter.initShoppingListCategories(shoppingListCategories)
    })
  }

  private fun initActionButtonVisibilityHandler() {
    shopping_list_recycler_view.onScrollChange { _, _, scrollY, _, _ ->
      confirm_button.visibility = if (scrollY > SHOW_ACTION_BUTTON_THRESHOLD) View.GONE else View.VISIBLE
    }
  }

  companion object {
    const val SHOPPING_LIST = "shoppingList"
    const val SHOPPING_CART = "shoppingCart"
    private const val SHOW_ACTION_BUTTON_THRESHOLD = 50

    fun newInstance() = ShoppingFragment()
  }
}
