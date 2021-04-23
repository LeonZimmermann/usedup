package de.usedup.android.shopping.preview

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
import de.usedup.android.datamodel.api.repositories.ImageRepository
import kotlinx.android.synthetic.main.shopping_list_preview_meal.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.anko.padding

class MealRecyclerAdapter(private val context: Context, private val coroutineScope: CoroutineScope) :
  RecyclerView.Adapter<MealRecyclerAdapter.MealViewHolder>() {

  private val entryPoint =
    EntryPointAccessors.fromApplication(context, MealRecyclerAdapterEntryPoint::class.java)
  private val inflater = LayoutInflater.from(context)
  private var meals: List<MealRepresentation> = emptyList()

  inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.meal_name_tv
    private val durationTv: TextView = itemView.dinner_item_duration_tv
    private val mealImageView: ImageView = itemView.meal_image_view

    fun init(meal: MealRepresentation) {
      meal.apply {
        nameTv.text = nameText
        durationTv.text = durationText
        imageName?.let { imageName ->
          coroutineScope.launch(Dispatchers.IO) {
            entryPoint.getImageRepository().getImage(imageName)
              .doOnError {
                Log.e(TAG, it.message ?: "An Error occurred while loading an image")
              }
              .subscribe {
                coroutineScope.launch(Dispatchers.Main) {
                  Glide.with(context)
                    .load(it)
                    .into(mealImageView)
                  mealImageView.apply {
                    imageTintMode = PorterDuff.Mode.DST
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    padding = 0
                  }
                }
              }
          }
        }
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

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface MealRecyclerAdapterEntryPoint {
    fun getImageRepository(): ImageRepository
  }

  companion object {
    private const val TAG = "MealRecyclerAdapter"
  }

}