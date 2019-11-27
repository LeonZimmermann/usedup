package com.hotmail.leon.zimmermann.homeassistant.ui.components.categoryOrder

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import kotlinx.android.synthetic.main.category_order_dialog.view.*
import java.io.Serializable

class CategoryOrderDialogFragment() : DialogFragment() {

    interface OnChangedListener : Serializable {
        fun onChanged(categoryOrder: MutableList<Pair<Int, Int>>)
    }

    private lateinit var categoryOrder: MutableList<Pair<Int, Int>>
    private lateinit var onChangedListener: OnChangedListener

    constructor(categoryOrder: List<Pair<Int, Int>>, onChanged: OnChangedListener) : this() {
        this.categoryOrder = categoryOrder.toMutableList()
        this.onChangedListener = onChanged
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            categoryOrder = (getSerializable(CATEGORY_ORDER) as Array<Pair<Int, Int>>).toMutableList()
            onChangedListener = getSerializable(ON_CHANGED_LISTENER) as OnChangedListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val view = requireActivity().layoutInflater.inflate(R.layout.category_order_dialog, null)
            view.category_order_list.adapter = CategoryOrderAdapter().apply {
                setCategoryOrder(categoryOrder)
                ItemTouchHelper(RecyclerViewHandler(this))
                    .attachToRecyclerView(view.category_order_list)
            }
            view.category_order_list.layoutManager = LinearLayoutManager(context)
            val builder = AlertDialog.Builder(it)
            builder.setView(view)
            builder.setNeutralButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
            builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
                onChangedListener.onChanged(categoryOrder)
            }
            return builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CATEGORY_ORDER, categoryOrder.toTypedArray())
        outState.putSerializable(ON_CHANGED_LISTENER, onChangedListener)
    }

    companion object {
        private const val CATEGORY_ORDER = "categoryOrder"
        private const val ON_CHANGED_LISTENER = "onChangedListener"
    }
}