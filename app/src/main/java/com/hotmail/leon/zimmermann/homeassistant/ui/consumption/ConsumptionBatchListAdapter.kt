package com.hotmail.leon.zimmermann.homeassistant.ui.consumption

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.product_item.view.*

class ConsumptionBatchListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<ConsumptionBatchListAdapter.ConsumptionBatchViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var consumptionList = mutableListOf<Pair<ProductEntity, Int>>()

    inner class ConsumptionBatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.product_name_tv
        val countView: TextView = itemView.count_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionBatchViewHolder {
        return ConsumptionBatchViewHolder(inflater.inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount() = consumptionList.size

    override fun onBindViewHolder(holder: ConsumptionBatchViewHolder, position: Int) {
        val (product, quantity) = consumptionList[position]
        holder.productNameView.text = product.name
        holder.countView.text = quantity.toString()
    }

    internal fun setConsumptionList(consumptionList: MutableList<Pair<ProductEntity, Int>>) {
        this.consumptionList = consumptionList
        notifyDataSetChanged()
    }
}