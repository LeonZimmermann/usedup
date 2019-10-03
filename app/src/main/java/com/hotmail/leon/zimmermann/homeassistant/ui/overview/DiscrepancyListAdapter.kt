package com.hotmail.leon.zimmermann.homeassistant.ui.overview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.product_item.view.*

class DiscrepancyListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<DiscrepancyListAdapter.DiscrepancyViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var productList = emptyList<ProductEntity>()

    inner class DiscrepancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.product_name_tv
        val discrepancyView: TextView = itemView.consumption_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscrepancyViewHolder {
        return DiscrepancyViewHolder(inflater.inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount() = productList.size

    override fun onBindViewHolder(holder: DiscrepancyViewHolder, position: Int) {
        val current = productList[position]
        holder.productNameView.text = current.name
        holder.discrepancyView.text = current.discrepancy.toString()
    }

    internal fun setProductList(productEntityList: List<ProductEntity>) {
        this.productList = productEntityList
        notifyDataSetChanged()
    }
}