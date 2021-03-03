package de.usedup.android.planner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.usedup.android.R
import de.usedup.android.utils.toDisplayString
import de.usedup.android.utils.toLocalDate
import de.usedup.android.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import de.usedup.android.datamodel.api.objects.PlannerItem
import de.usedup.android.datamodel.api.repositories.MealRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.android.synthetic.main.planner_item.view.*
import kotlinx.android.synthetic.main.planner_item.view.weekday_date_tv
import kotlinx.android.synthetic.main.planner_placeholder.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlannerRecyclerAdapter(private val context: Context, private val coroutineScope: CoroutineScope,
  private val callbacks: Callbacks) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerViewHandlerAdapter {

  private val entryPoint = EntryPointAccessors.fromApplication(context, PlannerRecyclerAdapterEntryPoint::class.java)
  private val mealRepository: MealRepository = entryPoint.getMealRepository()

  private var plannerItems: List<PlannerItem> = listOf()

  interface Callbacks {
    fun onPreviewButtonClicked(view: View, plannerItem: PlannerItem)
    fun onAddButtonClicked(view: View, date: LocalDate)
    fun onChangeButtonClicked(view: View, plannerItem: PlannerItem)
    fun onDeleteButtonClicked(view: View, plannerItem: PlannerItem)
  }

  inner class PlannerItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val weekdayDateTextView: TextView = itemView.weekday_date_tv
    val dinnerItemNameTextView: TextView = itemView.dinner_item_name_tv
    val dinnerItemDurationTextView: TextView = itemView.dinner_item_duration_tv
    val dinnerItemImageView: ImageView = itemView.dinner_item_image
    val previewButton: Button = itemView.preview_button
    val changeButton: Button = itemView.change_button
    val deleteButton: Button = itemView.delete_button
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

  private fun bindPlannerItemViewHolder(holder: PlannerItemViewHolder, position: Int) = coroutineScope.launch {
    val plannerItem = plannerItems[position]
    val date = plannerItem.date.toLocalDate()
    val meal = mealRepository.getMealForId(plannerItem.mealId)
    if (meal != null) {
      val weekdayText = date.dayOfWeek.toDisplayString()
      val dayText = date.dayOfMonth
      holder.weekdayDateTextView.text = "$weekdayText $dayText."
      holder.dinnerItemNameTextView.text = meal.name
      holder.dinnerItemDurationTextView.text = "${meal.duration}"
    } else {
      // TODO Find Better solution
      holder.weekdayDateTextView.text = "Error"
      holder.dinnerItemNameTextView.text = "Error"
      holder.dinnerItemDurationTextView.text = "0"
    }
    // TODO Init image
    // holder.dinnerItemImageView.image =
    holder.previewButton.setOnClickListener { callbacks.onPreviewButtonClicked(it, plannerItem) }
    holder.changeButton.setOnClickListener { callbacks.onChangeButtonClicked(it, plannerItem) }
    holder.deleteButton.setOnClickListener { callbacks.onDeleteButtonClicked(it, plannerItem) }
  }

  private fun bindPlannerPlaceholderViewHolder(holder: PlannerPlaceholderViewHolder, position: Int) {
    val date = plannerItems.lastOrNull()?.date?.toLocalDate()?.plusDays(1) ?: LocalDate.now()
    val weekdayText = date.dayOfWeek.toDisplayString()
    val dayText = date.dayOfMonth
    holder.weekdayDateTextView.text = "$weekdayText $dayText."
    holder.addButton.setOnClickListener { callbacks.onAddButtonClicked(it, date) }
  }

  override fun getItemViewType(position: Int): Int = if (position >= plannerItems.size) PLACEHOLDER_TYPE else ITEM_TYPE

  override fun getItemCount() = numberOfSlots() + 1

  private fun numberOfSlots(): Int {
    if (plannerItems.isEmpty()) return 0
    val firstDay = requireNotNull(plannerItems.minBy { it.date }).date
    val lastDay = requireNotNull(plannerItems.maxBy { it.date }).date
    return firstDay.millisToDays() - lastDay.millisToDays()
  }

  private fun Long.millisToDays(): Int = (this / (24 * 60 * 60 * 1000)).toInt()

  fun setPlannerItems(plannerItems: List<PlannerItem>) {
    this.plannerItems = plannerItems
    notifyDataSetChanged()
  }

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface PlannerRecyclerAdapterEntryPoint {
    fun getMealRepository(): MealRepository
  }

  companion object {
    private const val ITEM_TYPE = 0
    private const val PLACEHOLDER_TYPE = 1
  }
}