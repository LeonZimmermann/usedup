package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.ingredients.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.product_item.view.*

class ConsumptionIngredientsBatchListAdapter internal constructor(
    private val context: Context,
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<ConsumptionIngredientsBatchListAdapter.ConsumptionBatchViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var consumptionList = mutableListOf<ConsumptionIngredientsTemplate>()

    inner class ConsumptionBatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.product_name_tv
        val consumptionView: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionBatchViewHolder {
        val view = inflater.inflate(R.layout.product_item, parent, false)
        view.setOnClickListener(onClickListener)
        return ConsumptionBatchViewHolder(view)
    }

    override fun getItemCount() = consumptionList.size
    operator fun get(index: Int) = consumptionList[index]

    override fun onBindViewHolder(holder: ConsumptionBatchViewHolder, position: Int) {
        val consumption = consumptionList[position]
        holder.productNameView.text = consumption.product.name
        holder.consumptionView.text = context.resources.getString(
            R.string.consumption_quantity,
            consumption.value,
            consumption.measure.abbreviation
        )
    }

    internal fun setConsumptionList(consumptionList: MutableList<ConsumptionIngredientsTemplate>) {
        this.consumptionList = consumptionList
        notifyDataSetChanged()
    }
}