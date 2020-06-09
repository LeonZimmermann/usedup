package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.meal_editor_ingredients_item.view.*

class MealTemplateAdapter(private val context: Context, private val onItemRemoved: (Int) -> Unit) :
    RecyclerView.Adapter<MealTemplateAdapter.MealTemplateViewHolder>(), RecyclerViewHandlerAdapter {

    private var mealTemplateList: MutableList<MealTemplate> = mutableListOf()

    inner class MealTemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ingredientNameTextView: TextView = itemView.ingredient_name_tv
        val amountTextView: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealTemplateViewHolder {
        return MealTemplateViewHolder(
            LayoutInflater.from(context).inflate(R.layout.meal_editor_ingredients_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MealTemplateViewHolder, position: Int) {
        val consumptionTemplate = mealTemplateList[position]
        holder.ingredientNameTextView.text = consumptionTemplate.product.name
        holder.amountTextView.text = consumptionTemplate.value.toString()
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