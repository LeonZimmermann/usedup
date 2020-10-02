package com.hotmail.leon.zimmermann.homeassistant.app.ui.management

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Meal
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Template
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.product.ProductRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.TemplateRepository
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.android.synthetic.main.product_browser_item.view.*
import kotlinx.android.synthetic.main.template_browser_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManagementRecyclerAdapter constructor(context: Context, private val recyclerView: RecyclerView,
    private val navController: NavController,
    private val coroutineScope: CoroutineScope) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>(),
  com.hotmail.leon.zimmermann.homeassistant.components.recyclerViewHandler.RecyclerViewHandlerAdapter {

  private val inflater = LayoutInflater.from(context)

  var products: MutableList<Product> = mutableListOf()
    set(value) {
      field = value
      notifyDataSetChanged()
      recyclerView.scheduleLayoutAnimation()
    }

  var templates: MutableList<Template> = mutableListOf()
    set(value) {
      field = value
      notifyDataSetChanged()
      recyclerView.scheduleLayoutAnimation()
    }

  var meals: MutableList<Meal> = mutableListOf()
    set(value) {
      field = value
      notifyDataSetChanged()
      recyclerView.scheduleLayoutAnimation()
    }

  var mode: ManagementFragment.Mode = ManagementFragment.Mode.PRODUCT
    set(value) {
      field = value
      notifyDataSetChanged()
      recyclerView.scheduleLayoutAnimation()
    }

  inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.name_tv
    private val managementItemQuantityTv: TextView = itemView.management_item_quantity_tv
    private val managementItemCapacityTv: TextView = itemView.management_item_capacity_tv

    fun init(product: Product) {
      itemView.setOnClickListener {
        navController.navigate(
            R.id.action_management_fragment_to_product_editor_fragment, bundleOf(
                "productId" to product.id
            )
        )
      }
      product.apply {
        nameTv.text = name
        managementItemQuantityTv.text = quantity.toString()
        managementItemCapacityTv.text = capacity.toString()
      }
    }
  }

  inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val templateNameTv: TextView = itemView.template_name

    fun init(template: Template) {
      itemView.setOnClickListener {
        navController.navigate(
            R.id.action_management_fragment_to_template_editor_fragment, bundleOf(
                "templateId" to template.id
            )
        )
      }
      template.apply {
        templateNameTv.text = name
      }
    }
  }

  inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.dinner_item_name_tv
    private val durationTv: TextView = itemView.dinner_item_duration_tv
    private val shortDescriptionTv: TextView = itemView.dinner_item_short_description_tv

    fun init(meal: Meal) {
      itemView.setOnClickListener {
        navController.navigate(
            R.id.action_management_fragment_to_meal_editor_fragment, bundleOf(
                "mealId" to meal.id
            )
        )
      }
      meal.apply {
        nameTv.text = name
        durationTv.text = duration.toString()
        shortDescriptionTv.text = description
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
        ManagementFragment.Mode.PRODUCT.ordinal -> ProductViewHolder(
            inflater.inflate(
                R.layout.product_browser_item,
                parent,
                false
            )
        )
        ManagementFragment.Mode.TEMPLATE.ordinal -> TemplateViewHolder(
            inflater.inflate(
                R.layout.template_browser_item,
                parent,
                false
            )
        )
        ManagementFragment.Mode.MEAL.ordinal -> MealViewHolder(
            inflater.inflate(
                R.layout.meal_browser_item,
                parent,
                false
            )
        )
      else -> throw RuntimeException("Invalid viewType: $viewType")
    }
  }

  override fun getItemViewType(position: Int) = mode.ordinal

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (mode) {
        ManagementFragment.Mode.PRODUCT -> (holder as ProductViewHolder).init(products[position])
        ManagementFragment.Mode.TEMPLATE -> (holder as TemplateViewHolder).init(templates[position])
        ManagementFragment.Mode.MEAL -> (holder as MealViewHolder).init(meals[position])
    }
  }

  override fun getItemCount() = when (mode) {
      ManagementFragment.Mode.PRODUCT -> products.size
      ManagementFragment.Mode.TEMPLATE -> templates.size
      ManagementFragment.Mode.MEAL -> meals.size
  }

  override fun onItemDismiss(position: Int) {
    super.onItemDismiss(position)
    coroutineScope.launch {
      when (mode) {
          ManagementFragment.Mode.PRODUCT -> {
              val product = products[position]
              ProductRepository.deleteProduct(product.id)
              products.remove(product)
          }
          ManagementFragment.Mode.TEMPLATE -> {
              val template = templates[position]
              TemplateRepository.deleteTemplate(template.id)
              templates.remove(template)
          }
          ManagementFragment.Mode.MEAL -> {
              val meal = meals[position]
              MealRepository.deleteMeal(meal.id)
              meals.remove(meal)
          }
      }
      notifyItemRemoved(position)
    }
  }
}