package com.hotmail.leon.zimmermann.homeassistant.ui.components.categoryOrder

import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import kotlinx.android.synthetic.main.category_order_dialog.view.*
import java.io.Serializable

class CategoryOrderDialogFragment() : DialogFragment() {

    interface OnChangedListener : Serializable {
        fun onChanged(categoryOrder: List<Category>)
    }

    interface OnResetListener : Serializable {
        fun onReset()
    }

    private lateinit var categoryOrder: MutableList<Category>
    private lateinit var onChangedListener: OnChangedListener
    private lateinit var onResetListener: OnResetListener
    private lateinit var adapter: CategoryOrderAdapter

    constructor(
        categoryOrder: List<Category>,
        onChangedListener: OnChangedListener,
        onResetListener: OnResetListener
    ) : this() {
        this.categoryOrder = categoryOrder.toMutableList()
        this.onChangedListener = onChangedListener
        this.onResetListener = onResetListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            categoryOrder = (getSerializable(CATEGORY_ORDER) as Array<Category>).toMutableList()
            onChangedListener = getSerializable(ON_CHANGED_LISTENER) as OnChangedListener
            onResetListener = getSerializable(ON_RESET_LISTENER) as OnResetListener
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(createView())
            builder.setNeutralButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
            builder.setPositiveButton(R.string.submit) { dialogInterface, i ->
                if (adapter.hasChanged) onChangedListener.onChanged(categoryOrder)
            }
            builder.setNegativeButton(R.string.reset) { dialog, i -> onResetListener.onReset() }
            return builder.create()
        } ?: throw IllegalArgumentException("Activity cannot be null")
    }

    private fun createView(): View {
        val view = requireActivity().layoutInflater.inflate(R.layout.category_order_dialog, null)
        adapter = CategoryOrderAdapter().apply {
            setCategoryOrder(categoryOrder)
            ItemTouchHelper(RecyclerViewHandler(this))
                .attachToRecyclerView(view.category_order_list)
        }
        view.category_order_list.adapter = adapter
        view.category_order_list.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(CATEGORY_ORDER, categoryOrder.toTypedArray())
        outState.putSerializable(ON_CHANGED_LISTENER, onChangedListener)
        outState.putSerializable(ON_RESET_LISTENER, onResetListener)
    }

    companion object {
        private const val CATEGORY_ORDER = "categoryOrder"
        private const val ON_CHANGED_LISTENER = "onChangedListener"
        private const val ON_RESET_LISTENER = "onResetListener"
    }
}