package com.hotmail.leon.zimmermann.homeassistant.fragments.management

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.product.Product
import kotlinx.android.synthetic.main.management_item.view.*

class ManagementListAdapter internal constructor(context: Context, private val onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<ManagementListAdapter.ProductViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var productList = emptyList<Product>()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView = itemView.name_tv
        val productQuantityView = itemView.quantity_tv
        val productMinView = itemView.min_tv
        val productMaxView = itemView.max_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = inflater.inflate(R.layout.management_item, parent, false)
        itemView.setOnClickListener(onClickListener)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = productList[position]
        holder.productNameView.text = current.name
        holder.productQuantityView.text = current.quantity.toString()
        holder.productMinView.text = current.min.toString()
        holder.productMaxView.text = current.max.toString()
    }

    internal fun setProductList(productList: List<Product>) {
        this.productList = productList
        notifyDataSetChanged()
    }

    operator fun get(position: Int): Product = productList[position]

    override fun getItemCount(): Int = productList.size
}