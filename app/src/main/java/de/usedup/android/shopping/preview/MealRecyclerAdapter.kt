package de.usedup.android.shopping.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.usedup.android.R
import kotlinx.android.synthetic.main.shopping_list_preview_meal.view.*

class MealRecyclerAdapter(private val context: Context) : RecyclerView.Adapter<MealRecyclerAdapter.MealViewHolder>() {

  private val inflater = LayoutInflater.from(context)
  private var meals: List<MealRepresentation> = emptyList()

  inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.meal_name_tv
    private val durationTv: TextView = itemView.dinner_item_duration_tv
    private val imageView: ImageView = itemView.meal_image_view

    fun init(meal: MealRepresentation) {
      meal.apply {
        nameTv.text = nameText
        durationTv.text = durationText
        // TODO Init imageView
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
    return MealViewHolder(inflater.inflate(R.layout.shopping_list_preview_meal, parent, false))
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