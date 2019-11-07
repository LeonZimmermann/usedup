package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.shopping_item.view.*

class ShoppingListAdapter(context: Context) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var productEntityList: List<ProductEntity> = emptyList()
    var checkedProducts: MutableList<Int> = mutableListOf()
        private set

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkbox = itemView.shopping_item_checkbox
        val quantity = itemView.shopping_item_quantity_input
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder {
        val itemView = inflater.inflate(R.layout.shopping_item, parent, false)
        return ShoppingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val currentProduct = productEntityList[position]
        holder.checkbox.text = currentProduct.name
        holder.quantity.text = currentProduct.discrepancy.toString()
        holder.itemView.setOnClickListener { updateCheckedProductsList(currentProduct, holder.checkbox) }
    }

    private fun updateCheckedProductsList(productEntity: ProductEntity, checkbox: CheckBox) {
        if (checkedProducts.contains(productEntity.id)) {
            checkedProducts.remove(productEntity.id)
            checkbox.isChecked = false
        } else {
            checkedProducts.add(productEntity.id)
            checkbox.isChecked = true
        }
    }

    override fun getItemCount() = productEntityList.size

    internal fun setProductList(productEntityList: List<ProductEntity>) {
        this.productEntityList = productEntityList
        checkedProducts.clear()
        notifyDataSetChanged()
    }
}