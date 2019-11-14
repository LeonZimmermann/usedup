package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.shopping_item.view.*

class ShoppingListAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var shoppingList: MutableList<ShoppingEntry> = mutableListOf()
    var checkedEntries: MutableList<Int> = mutableListOf()
        private set

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product: TextView = itemView.shopping_item_name_tv
        val amount: TextView = itemView.shopping_item_quantity_input
        val checkbox: CheckBox = itemView.shopping_item_checkbox
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView = inflater.inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val currentEntry = shoppingList[position]
        holder.product.text = currentEntry.product.name
        holder.amount.text = context.getString(R.string.shopping_entry_amount, currentEntry.amount)
        holder.itemView.setOnClickListener { updateCheckedProductsList(currentEntry.product, holder.checkbox) }
    }

    private fun updateCheckedProductsList(productEntity: ProductEntity, checkbox: CheckBox) {
        if (checkedEntries.contains(productEntity.id)) {
            checkedEntries.remove(productEntity.id)
            checkbox.isChecked = false
        } else {
            checkedEntries.add(productEntity.id)
            checkbox.isChecked = true
        }
    }

    override fun getItemCount() = shoppingList.size

    internal fun setShoppingList(shoppingList: MutableList<ShoppingEntry>) {
        this.shoppingList = shoppingList
        checkedEntries.clear()
        notifyDataSetChanged()
    }
}