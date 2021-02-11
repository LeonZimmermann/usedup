package de.usedup.android.components.consumption

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.usedup.android.R
import de.usedup.android.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.consumption_element.view.*

class ConsumptionElementAdapter(private val context: Context, private val onItemRemoved: (Int) -> Unit) :
    RecyclerView.Adapter<ConsumptionElementAdapter.ConsumptionElementViewHolder>(), RecyclerViewHandlerAdapter {

    private var consumptionElementList: MutableList<ConsumptionElement> = mutableListOf()

    inner class ConsumptionElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameTextView: TextView = itemView.ingredient_name_tv
        val amountTextView: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionElementViewHolder {
        return ConsumptionElementViewHolder(
            LayoutInflater.from(context).inflate(R.layout.consumption_element, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConsumptionElementViewHolder, position: Int) {
        val consumptionTemplate = consumptionElementList[position]
        holder.ingredientNameTextView.text = consumptionTemplate.product.name
        holder.amountTextView.text = consumptionTemplate.valueValue.toString()
    }

    override fun getItemCount() = consumptionElementList.size

    fun setConsumptionElementList(consumptionElementList: MutableList<ConsumptionElement>) {
        this.consumptionElementList = consumptionElementList
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {
        onItemRemoved(position)
        notifyItemRemoved(position)
    }
}