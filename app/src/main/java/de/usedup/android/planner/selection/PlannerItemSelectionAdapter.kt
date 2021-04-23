package de.usedup.android.planner.selection

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.utils.toDurationString
import kotlinx.android.synthetic.main.meal_browser_item.view.*

class PlannerItemSelectionAdapter(private val context: Context, private val callback: Callback) :
  RecyclerView.Adapter<PlannerItemSelectionAdapter.PlannerItemSelectionViewHolder>() {

  private val inflater = LayoutInflater.from(context)
  private var mealList: List<Meal> = listOf()

  inner class PlannerItemSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val dinnerItemImage: ImageView = itemView.dinner_item_image
    val dinnerItemName: TextView = itemView.dinner_item_name_tv
    val dinnerItemDuration: TextView = itemView.dinner_item_duration_tv
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerItemSelectionViewHolder {
    return PlannerItemSelectionViewHolder(inflater.inflate(R.layout.meal_browser_item, parent, false))
  }

  override fun onBindViewHolder(holder: PlannerItemSelectionViewHolder, position: Int) {
    val meal = mealList[position]
    meal.imageName?.let {
      Glide.with(context)
        .load(it)
        .into(holder.dinnerItemImage)
    }
    holder.dinnerItemName.text = meal.name
    holder.dinnerItemDuration.text = meal.duration.toDurationString()
    holder.itemView.setOnClickListener { callback.onMealSelected(holder.itemView, meal) }
  }

  override fun getItemCount(): Int = mealList.size

  internal fun setMealList(mealList: List<Meal>) {
    this.mealList = mealList
    notifyDataSetChanged()
  }

  interface Callback {
    fun onMealSelected(view: View, meal: Meal)
  }
}