package com.hotmail.leon.zimmermann.homeassistant.fragments.shopping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.product.Product
import kotlinx.android.synthetic.main.shopping_item.view.*

class ShoppingListAdapter(context: Context) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var productList: List<Product> = emptyList()
    var checkedProducts: MutableList<Int> = mutableListOf()
        private set

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox = itemView.checkbox
        val quantity = itemView.quantity_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView = inflater.inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.checkbox.text = currentProduct.name
        holder.quantity.text = currentProduct.discrepancy.toString()
        holder.itemView.setOnClickListener { updateCheckedProductsList(currentProduct, holder.checkbox) }
    }

    private fun updateCheckedProductsList(product: Product, checkbox: CheckBox) {
        if (checkedProducts.contains(product.id)) {
            checkedProducts.remove(product.id)
            checkbox.isChecked = false
        } else {
            checkedProducts.add(product.id)
            checkbox.isChecked = true
        }
    }

    override fun getItemCount() = productList.size

    internal fun setProductList(productList: List<Product>) {
        this.productList = productList
        checkedProducts.clear()
        notifyDataSetChanged()
    }
}