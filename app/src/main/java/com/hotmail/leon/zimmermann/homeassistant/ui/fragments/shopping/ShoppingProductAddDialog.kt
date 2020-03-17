package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_product_add_dialog.view.*
import org.jetbrains.anko.support.v4.toast

class ShoppingProductAddDialog(private val productList: List<String>, private val callback: (String, Int) -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            val view = requireActivity().layoutInflater.inflate(R.layout.shopping_product_add_dialog, null)
            view.shopping_product_name_input.setAdapter(ArrayAdapter<String>(
                context!!,
                android.R.layout.simple_list_item_1,
                productList
            ))
            view.shopping_product_cart_amount_input.let {
                it.minValue = 0
                it.maxValue = 20
            }
            builder.setTitle(R.string.number_picker)
            builder.setView(view)
            builder.setPositiveButton(R.string.submit) { _, _ ->
                val productName = view.shopping_product_name_input.text.toString()
                if (!productList.contains(productName)) toast("Product Name $productName is invalid")
                else callback(productName, view.shopping_product_cart_amount_input.value)
            }
            builder.setNeutralButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
            builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }
}