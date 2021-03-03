package de.usedup.android.shopping.preview

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.usedup.android.R
import de.usedup.android.shopping.data.ShoppingProduct
import kotlinx.android.synthetic.main.shopping_product_dialog.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.Serializable

class ShoppingProductDialog(private var titleStringId: Int = -1, private var callback: Callback? = null) :
  DialogFragment() {

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
          applyData(view)
          viewModel.dialogEditShoppingProduct = null
          viewModel.shoppingProductDialog.postValue(null)
        }
        .setNegativeButton(R.string.cancel) { dialogInterface, _ ->
          viewModel.dialogEditShoppingProduct = null
          dialogInterface.cancel()
        }
        .setOnDismissListener { viewModel.shoppingProductDialog.postValue(null) }
        .setOnCancelListener { viewModel.shoppingProductDialog.postValue(null) }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }

  private fun initView(view: View) {
    viewModel.productNames.observe(this, Observer { productNames ->
      view.product_name_input.setAdapter(
        ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, productNames))
    })
    view.dialog_title.text = resources.getString(titleStringId)
    viewModel.dialogEditShoppingProduct?.let { view.product_name_input.setText(it.product.name) }
    view.cart_amount_input.setText(viewModel.dialogEditShoppingProduct?.cartAmount?.toString() ?: "0")
  }

  private fun applyData(view: View) = viewModel.viewModelScope.launch(Dispatchers.IO) {
    val productName = view.product_name_input.text.toString()
    val product = viewModel.getProductForName(productName)
    if (product != null) {
      val cartAmount = view.cart_amount_input.text.toString().toIntOrNull() ?: throw IllegalArgumentException()
      withContext(Dispatchers.Main) {
        callback?.onResult(ShoppingProduct(product, cartAmount))
      }
    }
  }

  private fun initWithSavedState(savedInstanceState: Bundle?) {
    savedInstanceState?.let {
      titleStringId = it.getInt(TITLE_STRING_ID, -1)
      if (titleStringId == -1) throw RuntimeException()
      callback = it.getSerializable(CALLBACK) as? Callback ?: throw RuntimeException()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    outState.putInt(TITLE_STRING_ID, titleStringId)
    outState.putSerializable(CALLBACK, callback)
  }

  @FunctionalInterface
  interface Callback : Serializable {
    fun onResult(shoppingProduct: ShoppingProduct)
  }

  companion object {
    private const val TITLE_STRING_ID = "TITLE_STRING_ID"
    private const val CALLBACK = "CALLBACK"
  }

}