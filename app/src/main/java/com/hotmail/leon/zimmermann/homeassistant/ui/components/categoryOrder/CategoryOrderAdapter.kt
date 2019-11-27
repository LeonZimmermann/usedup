package com.hotmail.leon.zimmermann.homeassistant.ui.components.categoryOrder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.shopping_category_item.view.*
import java.util.*

class CategoryOrderAdapter :
    RecyclerView.Adapter<CategoryOrderAdapter.CategoryOrderViewHolder>(), RecyclerViewHandlerAdapter {

    private lateinit var categoryOrder: MutableList<Category>
    var hasChanged: Boolean = false

    inner class CategoryOrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.category_name_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryOrderViewHolder {
        return CategoryOrderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.category_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CategoryOrderViewHolder, position: Int) {
        holder.textView.text = categoryOrder[position].name
    }

    override fun onItemDismiss(position: Int) {
        categoryOrder.removeAt(position)
        hasChanged = true
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) for (i in fromPosition until toPosition) Collections.swap(
            categoryOrder, i, i + 1
        )
        else for (i in fromPosition downTo toPosition + 1) Collections.swap(categoryOrder, i, i - 1)
        hasChanged = true
        notifyItemMoved(fromPosition, toPosition)
    }

    internal fun setCategoryOrder(categoryOrder: MutableList<Category>) {
        this.categoryOrder = categoryOrder
    }

    override fun getItemCount(): Int = categoryOrder.size
}