package com.hotmail.leon.zimmermann.homeassistant.ui.components.picker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import kotlinx.android.synthetic.main.dinner_item.view.*

class DinnerListAdapter(context: Context, private val  onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<DinnerListAdapter.DinnerViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var consumptionLists = emptyList<ConsumptionList>()

    inner class DinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.dinner_item_name_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DinnerViewHolder {
        val itemView = inflater.inflate(R.layout.dinner_item, parent, false)
        itemView.setOnClickListener(onClickListener)
        return DinnerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DinnerViewHolder, position: Int) {
        val current = consumptionLists[position]
        holder.nameView.text = current.metaData.name
    }

    internal fun setConsumptionLists(consumptionLists: List<ConsumptionList>) {
        this.consumptionLists = consumptionLists
        notifyDataSetChanged()
    }

    internal operator fun get(position: Int) = consumptionLists[position]

    override fun getItemCount(): Int = consumptionLists.size
}