package com.hotmail.leon.zimmermann.homeassistant.ui.consumption

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionData
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionRepository
import kotlinx.android.synthetic.main.product_item.view.*

class ConsumptionBatchListAdapter internal constructor(private val context: Context) :
    RecyclerView.Adapter<ConsumptionBatchListAdapter.ConsumptionBatchViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var consumptionList = mutableListOf<ConsumptionData>()

    inner class ConsumptionBatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.product_name_tv
        val consumptionView: TextView = itemView.consumption_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionBatchViewHolder {
        return ConsumptionBatchViewHolder(inflater.inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount() = consumptionList.size

    override fun onBindViewHolder(holder: ConsumptionBatchViewHolder, position: Int) {
        val consumption = consumptionList[position]
        holder.productNameView.text = consumption.product.name
        holder.consumptionView.text = context.resources.getString(
            R.string.consumption_quantity,
            consumption.value,
            consumption.measure.abbreviation
        )
    }

    internal fun setConsumptionList(consumptionList: MutableList<ConsumptionData>) {
        this.consumptionList = consumptionList
        notifyDataSetChanged()
    }
}