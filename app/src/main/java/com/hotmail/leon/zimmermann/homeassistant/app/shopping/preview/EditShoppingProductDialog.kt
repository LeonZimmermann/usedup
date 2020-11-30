package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.shopping.data.ShoppingProduct

class EditShoppingProductDialog(
  private val shoppingProduct: ShoppingProduct? = null,
  private val callback: Callback
): DialogFragment() {

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      val view = requireActivity().layoutInflater.inflate(R.layout.edit_shopping_product_dialog, null)
      MaterialAlertDialogBuilder(requireContext())
        .setView(view)
        .setPositiveButton(R.string.submit) { _, _ -> callback.onPositiveButtonClicked(shoppingProduct!!) }
        .setNegativeButton(R.string.cancel) { _, _ -> callback.onNegativeButtonClicked() }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }

  interface Callback {
    fun onPositiveButtonClicked(shoppingProduct: ShoppingProduct)
    fun onNegativeButtonClicked()
  }
}