package com.hotmail.leon.zimmermann.homeassistant.app.planner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.app.toDisplayString
import com.hotmail.leon.zimmermann.homeassistant.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.android.synthetic.main.planner_item.view.*
import kotlinx.android.synthetic.main.planner_item.view.weekday_date_tv
import kotlinx.android.synthetic.main.planner_placeholder.view.*
import java.time.LocalDate

class PlannerRecyclerAdapter(private val context: Context, private val callbacks: Callbacks) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerViewHandlerAdapter {

  private var plannerItems: List<PlannerItem> = listOf()

  interface Callbacks {
    fun onPreviewButtonClicked(view: View, plannerItem: PlannerItem)
    fun onAddButtonClicked(view: View, date: LocalDate)
    fun onChangeButtonClicked(view: View, plannerItem: PlannerItem)
  }

  inner class PlannerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val weekdayDateTextView: TextView = itemView.weekday_date_tv
    val dinnerItemNameTextView: TextView = itemView.dinner_item_name_tv
    val dinnerItemDurationTextView: TextView = itemView.dinner_item_duration_tv
    val dinnerItemImageView: ImageView = itemView.dinner_item_image
    val previewButton: Button = itemView.preview_button
    val changeButton: Button = itemView.change_button
  }

  inner class PlannerPlaceholderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val weekdayDateTextView: TextView = itemView.weekday_date_tv
    val addButton: Button = itemView.add_button
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      ITEM_TYPE -> PlannerItemViewHolder(LayoutInflater.from(context).inflate(R.layout.planner_item, parent, false))
      PLACEHOLDER_TYPE -> PlannerPlaceholderViewHolder(
        LayoutInflater.from(context).inflate(R.layout.planner_placeholder, parent, false))
      else -> throw IllegalArgumentException()
    }

  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder) {
      is PlannerItemViewHolder -> bindPlannerItemViewHolder(holder, position)
      is PlannerPlaceholderViewHolder -> bindPlannerPlaceholderViewHolder(holder, position)
    }
  }

  private fun bindPlannerItemViewHolder(holder: PlannerItemViewHolder, position: Int) {
    val plannerItem = plannerItems[position]
    val weekdayText = plannerItem.date.dayOfWeek.toDisplayString()
    val dayText = plannerItem.date.dayOfMonth
    holder.weekdayDateTextView.text = "$weekdayText $dayText."
    holder.dinnerItemNameTextView.text = plannerItem.meal.name
    holder.dinnerItemDurationTextView.text = "${plannerItem.meal.duration}"
    // TODO Init image
    // holder.dinnerItemImageView.image =
    holder.previewButton.setOnClickListener { callbacks.onPreviewButtonClicked(it, plannerItem) }
    holder.changeButton.setOnClickListener { callbacks.onChangeButtonClicked(it, plannerItem) }
  }

  private fun bindPlannerPlaceholderViewHolder(holder: PlannerPlaceholderViewHolder, position: Int) {
    val date = plannerItems.lastOrNull()?.date?.plusDays(1) ?: LocalDate.now()
    val weekdayText = date.dayOfWeek.toDisplayString()
    val dayText = date.dayOfMonth
    holder.weekdayDateTextView.text = "$weekdayText $dayText."
    holder.addButton.setOnClickListener { callbacks.onAddButtonClicked(it, date) }
  }

  override fun getItemViewType(position: Int): Int = if (position >= plannerItems.size) PLACEHOLDER_TYPE else ITEM_TYPE

  override fun getItemCount() = plannerItems.size + 1

  fun setPlannerItems(plannerItems: List<PlannerItem>) {
    this.plannerItems = plannerItems
    notifyDataSetChanged()
  }

  companion object {
    private const val ITEM_TYPE = 0
    private const val PLACEHOLDER_TYPE = 1
  }
}