package com.hotmail.leon.zimmermann.homeassistant.app.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
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
    initSystemMessage()
  }

  private fun initViewModel() {
    viewModel = activity?.run {
      ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
    } ?: throw Exception("Invalid Activity")
  }

  private fun initAdapter() {
    adapter = ShoppingListAdapter(context!!)
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
