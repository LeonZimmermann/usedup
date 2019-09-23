package com.hotmail.leon.zimmermann.homeassistant.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.product.Product
import kotlinx.android.synthetic.main.overview_discrepancy_item.view.*

class DiscrepancyListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<DiscrepancyListAdapter.DiscrepancyViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var productList = emptyList<Product>()

    inner class DiscrepancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView = itemView.product_name_tv
        val discrepancyView = itemView.discrepancy_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscrepancyViewHolder {
        return DiscrepancyViewHolder(inflater.inflate(R.layout.overview_discrepancy_item, parent, false))
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: DiscrepancyViewHolder, position: Int) {
        val current = productList[position]
        holder.productNameView.text = current.name
        holder.discrepancyView.text = current.discrepancy.toString()
    }

    internal fun setProductList(productList: List<Product>) {
        this.productList = productList
        notifyDataSetChanged()
    }
}