package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.app.toDisplayString
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.android.synthetic.main.planner_item.view.*

class PlannerRecyclerAdapter(private val context: Context, private val onClickCallback: (PlannerItem) -> Unit) :
  RecyclerView.Adapter<PlannerRecyclerAdapter.PlannerItemViewHolder>(), RecyclerViewHandlerAdapter {

  private var plannerItems: List<PlannerItem> = listOf()

  inner class PlannerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val weekdayDateTextView: TextView = itemView.weekday_date_tv
    val dinnerItemNameTextView: TextView = itemView.dinner_item_name_tv
    val dinnerItemDurationTextView: TextView = itemView.dinner_item_duration_tv
    val dinnerItemImageView: ImageView = itemView.dinner_item_image
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlannerItemViewHolder {
    return PlannerItemViewHolder(LayoutInflater.from(context).inflate(R.layout.planner_item, parent, false))
  }

  override fun onBindViewHolder(holder: PlannerItemViewHolder, position: Int) {
    val plannerItem = plannerItems[position]
    val weekdayText = plannerItem.date.dayOfWeek.toDisplayString()
    val dayText = plannerItem.date.dayOfMonth
    holder.weekdayDateTextView.text = "$weekdayText $dayText."
    holder.dinnerItemNameTextView.text = plannerItem.meal.name
    holder.dinnerItemDurationTextView.text = "${plannerItem.meal.duration}"
    // TODO Init image
    // holder.dinnerItemImageView.image =
    holder.itemView.setOnClickListener { onClickCallback(plannerItem) }
  }

  override fun getItemCount() = plannerItems.size

  fun setPlannerItems(plannerItems: List<PlannerItem>) {
    this.plannerItems = plannerItems
    notifyDataSetChanged()
  }
}