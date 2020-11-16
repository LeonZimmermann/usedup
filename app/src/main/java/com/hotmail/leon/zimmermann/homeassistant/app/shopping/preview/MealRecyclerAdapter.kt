package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.meal_browser_item.view.*

class MealRecyclerAdapter(context: Context) : RecyclerView.Adapter<MealRecyclerAdapter.MealViewHolder>() {

  private val inflater = LayoutInflater.from(context)
  private var meals: List<MealRepresentation> = emptyList()

  inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.dinner_item_name_tv
    private val durationTv: TextView = itemView.dinner_item_duration_tv

    fun init(meal: MealRepresentation) {
      meal.apply {
        nameTv.text = nameText
        durationTv.text = durationText
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
    return MealViewHolder(inflater.inflate(R.layout.meal_browser_item, parent, false))
  }

  override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
    holder.init(meals[position])
  }

  override fun getItemCount(): Int = meals.size

  internal fun setMealList(meals: List<MealRepresentation>) {
    this.meals = meals
    notifyDataSetChanged()
  }

}