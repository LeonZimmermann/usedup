package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.edit

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.ShoppingEntry
import kotlinx.android.synthetic.main.product_item.view.*
import kotlinx.android.synthetic.main.shopping_item.view.*


class ShoppingEditListAdapter(private val context: Context, private val onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<ShoppingEditListAdapter.ShoppingEditViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var shoppingList: MutableList<ShoppingEntry> = mutableListOf()

    inner class ShoppingEditViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product: TextView = itemView.product_name_tv
        val amount: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingEditViewHolder {
        val itemView = inflater.inflate(R.layout.product_item, parent, false)
        itemView.setOnClickListener(onClickListener)
        return ShoppingEditViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingEditViewHolder, position: Int) {
        val currentEntry = shoppingList[position]
        holder.product.text = currentEntry.product.name
        holder.amount.text = context.getString(R.string.shopping_entry_amount, currentEntry.amount)
    }

    override fun getItemCount() = shoppingList.size

    internal fun setShoppingList(shoppingList: MutableList<ShoppingEntry>) {
        this.shoppingList = shoppingList
        notifyDataSetChanged()
    }
}