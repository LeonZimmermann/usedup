package com.hotmail.leon.zimmermann.homeassistant.ui.browser

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import kotlinx.android.synthetic.main.consumption_browser_item.view.*

class ConsumptionBrowserListAdapter(private val context: Context) :
    RecyclerView.Adapter<ConsumptionBrowserListAdapter.ConsumptionBrowserViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var consumptionLists = emptyList<ConsumptionList>()

    inner class ConsumptionBrowserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.consumption_list_name
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionBrowserViewHolder {
        val itemView = inflater.inflate(R.layout.consumption_browser_item, parent, false)
        return ConsumptionBrowserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ConsumptionBrowserViewHolder, position: Int) {
        val current = consumptionLists[position]
        holder.nameView.text = current.metaData.name
    }

    internal fun setConsumptionLists(consumptionLists: List<ConsumptionList>) {
        this.consumptionLists = consumptionLists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = consumptionLists.size
}