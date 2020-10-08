package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shopping_fragment.*

@AndroidEntryPoint
class ShoppingFragment : Fragment() {

  private val viewModel: ShoppingViewModel by viewModels()
  private lateinit var adapter: ShoppingListAdapter

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
    initSystemMessage()
  }

  override fun onStart() {
    super.onStart()
    viewModel.createShoppingList()
  }

  private fun initAdapter() {
    adapter = ShoppingListAdapter(requireContext())
    viewModel.shoppingList.observe(viewLifecycleOwner, Observer { shoppingList ->
      adapter.setData(shoppingList)
    })
  }

  private fun initShoppingList() {
    shopping_list.adapter = adapter
    shopping_list.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
  }

  private fun initConfirmButton() {
    confirm_button.setOnClickListener {
      viewModel.submitTransaction()
      findNavController().navigateUp()
    }
  }

  private fun initSystemMessage() {
    viewModel.systemMessage.observe(viewLifecycleOwner, Observer { systemMessage ->
      Snackbar.make(requireView(), systemMessage, Snackbar.LENGTH_LONG).show()
    })
  }

  companion object {
    fun newInstance() = ShoppingFragment()
  }
}
