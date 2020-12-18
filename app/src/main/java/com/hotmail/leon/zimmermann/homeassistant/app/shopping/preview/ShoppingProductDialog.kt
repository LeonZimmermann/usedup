package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_product_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShoppingProductDialog(private var titleStringId: Int = -1) : DialogFragment() {

  private lateinit var viewModel: ShoppingListPreviewViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProvider(requireActivity())[ShoppingListPreviewViewModel::class.java]
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    initWithSavedState(savedInstanceState)
    return activity?.let {
      val view = requireActivity().layoutInflater.inflate(R.layout.shopping_product_dialog, null)
      initView(view)
      MaterialAlertDialogBuilder(requireContext())
        .setView(view)
        .setPositiveButton(R.string.submit) { _, _ ->
          applyData()
          viewModel.editShoppingProduct = null
        }
        .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
          viewModel.editShoppingProduct = null
          dialogInterface.cancel()
        }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }

  private fun initView(view: View) {
    viewModel.productNames.observe(this, Observer { productNames ->
      view.product_name_input.setAdapter(
        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, productNames))
    })
    view.dialog_title.text = resources.getString(titleStringId)
    viewModel.editShoppingProduct?.let { view.product_name_input.setText(it.product.name) }
    view.cart_amount_input.setText(viewModel.editShoppingProduct?.cartAmount?.toString() ?: "0")
  }

  private fun applyData() = viewModel.viewModelScope.launch(Dispatchers.IO) {
    val productName = requireView().product_name_input.text.toString()
    val product = viewModel.getProductForName(productName);
    val cartAmount = requireView().cart_amount_input.text.toString().toIntOrNull() ?: throw IllegalArgumentException()
    // TODO Update data
  }

  private fun initWithSavedState(savedInstanceState: Bundle?) {
    savedInstanceState?.let {
      titleStringId = it.getInt(TITLE_STRING_ID, -1)
      if (titleStringId == -1) throw RuntimeException()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(TITLE_STRING_ID, titleStringId)
  }

  companion object {
    private const val TITLE_STRING_ID = "TITLE_STRING_ID"
  }

}