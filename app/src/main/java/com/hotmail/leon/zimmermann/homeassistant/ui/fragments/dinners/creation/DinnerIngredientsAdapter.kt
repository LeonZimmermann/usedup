package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.dinners.creation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.dinner_creation_ingredients_item.view.*

class DinnerIngredientsAdapter(private val context: Context, private val onItemRemoved: (Int) -> Unit) :
    RecyclerView.Adapter<DinnerIngredientsAdapter.ConsumptionIngredientsViewHolder>(), RecyclerViewHandlerAdapter {

    private var dinnerTemplateList: MutableList<DinnerTemplate> = mutableListOf()

    inner class ConsumptionIngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameTextView: TextView = itemView.ingredient_name_tv
        val amountTextView: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionIngredientsViewHolder {
        return ConsumptionIngredientsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.dinner_ingredients_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConsumptionIngredientsViewHolder, position: Int) {
        val consumptionTemplate = dinnerTemplateList[position]
        holder.ingredientNameTextView.text = consumptionTemplate.product.name
        holder.amountTextView.text = consumptionTemplate.value.toString() + consumptionTemplate.measure.abbreviation
    }

    override fun getItemCount() = dinnerTemplateList.size

    internal fun setConsumptionEntityList(dinnerTemplateList: MutableList<DinnerTemplate>) {
        this.dinnerTemplateList = dinnerTemplateList
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {
        onItemRemoved(position)
        notifyItemRemoved(position)
    }
}