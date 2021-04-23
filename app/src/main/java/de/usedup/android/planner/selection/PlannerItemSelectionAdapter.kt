package de.usedup.android.planner.selection

import android.content.Context
import android.graphics.PorterDuff
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.datamodel.api.repositories.ImageRepository
import de.usedup.android.planner.PlannerRecyclerAdapter
import de.usedup.android.utils.toDurationString
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.padding

class PlannerItemSelectionAdapter(
  private val context: Context,
  private val coroutineScope: CoroutineScope,
  private val callback: Callback) :
  RecyclerView.Adapter<PlannerItemSelectionAdapter.PlannerItemSelectionViewHolder>() {

  private val entryPoint =
    EntryPointAccessors.fromApplication(context, PlannerItemSelectionAdapterEntryPoint::class.java)
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
    meal.imageName?.let { imageName ->
      coroutineScope.launch(Dispatchers.IO) {
        entryPoint.getImageRepository().getImage(imageName)
          .doOnError { Log.e(TAG, it.message ?: "An Error occurred while loading an image") }
          .subscribe {
            coroutineScope.launch(Dispatchers.Main) {
              Glide.with(context)
                .load(it)
                .into(holder.dinnerItemImage)
              holder.dinnerItemImage.apply {
                imageTintMode = PorterDuff.Mode.DST
                scaleType = ImageView.ScaleType.CENTER_CROP
                padding = 0
              }
            }
          }
      }
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

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface PlannerItemSelectionAdapterEntryPoint {
    fun getImageRepository(): ImageRepository
  }

  interface Callback {
    fun onMealSelected(view: View, meal: Meal)
  }

  companion object {
    private const val TAG = "PlannerItemSelectionAdapter"
  }
}