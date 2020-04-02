package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.creation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.dinner_creation_ingredients_item.view.*

class MealIngredientsAdapter(private val context: Context, private val onItemRemoved: (Int) -> Unit) :
    RecyclerView.Adapter<MealIngredientsAdapter.ConsumptionIngredientsViewHolder>(), RecyclerViewHandlerAdapter {

    private var mealTemplateList: MutableList<MealTemplate> = mutableListOf()

    inner class ConsumptionIngredientsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameTextView: TextView = itemView.ingredient_name_tv
        val amountTextView: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsumptionIngredientsViewHolder {
        return ConsumptionIngredientsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.dinner_creation_ingredients_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ConsumptionIngredientsViewHolder, position: Int) {
        val consumptionTemplate = mealTemplateList[position]
        holder.ingredientNameTextView.text = consumptionTemplate.product.name
        holder.amountTextView.text = consumptionTemplate.value.toString() + consumptionTemplate.measure.abbreviation
    }

    override fun getItemCount() = mealTemplateList.size

    internal fun setConsumptionEntityList(mealTemplateList: MutableList<MealTemplate>) {
        this.mealTemplateList = mealTemplateList
        notifyDataSetChanged()
    }

    override fun onItemDismiss(position: Int) {
        onItemRemoved(position)
        notifyItemRemoved(position)
    }
}