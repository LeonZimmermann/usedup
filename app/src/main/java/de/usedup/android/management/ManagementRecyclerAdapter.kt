package de.usedup.android.management

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import de.usedup.android.R
import de.usedup.android.utils.toFloatFormat
import de.usedup.android.utils.toIntFormat
import de.usedup.android.components.recyclerViewHandler.RecyclerViewHandlerAdapter
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.objects.Template
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.TemplateRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import de.usedup.android.utils.toDurationString
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.android.synthetic.main.product_browser_item.view.*
import kotlinx.android.synthetic.main.template_browser_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ManagementRecyclerAdapter constructor(context: Context, private val recyclerView: RecyclerView,
  private val navController: NavController, private val coroutineScope: CoroutineScope) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerViewHandlerAdapter {

  private val entryPoint = EntryPointAccessors.fromApplication(context, ManagementRecyclerAdapterEntryPoint::class.java)
  private val productRepository: ProductRepository = entryPoint.getProductRepository()
  private val templateRepository: TemplateRepository = entryPoint.getTemplateRepository()
  private val mealRepository: MealRepository = entryPoint.getMealRepository()

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

  var mode: ManagementViewModel.Mode = ManagementViewModel.Mode.PRODUCT
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
        navController.navigate(R.id.action_management_fragment_to_product_editor_fragment,
          bundleOf("productId" to product.id))
      }
      product.apply {
        nameTv.text = name
        managementItemQuantityTv.text =
          "Quantity: ${quantity.toFloatFormat()} (${min.toIntFormat()} - ${max.toIntFormat()})"
        managementItemCapacityTv.text = "Capacity: ${capacity.toFloatFormat()}"
      }
    }
  }

  inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val templateNameTv: TextView = itemView.template_name

    fun init(template: Template) {
      itemView.setOnClickListener {
        navController.navigate(R.id.action_management_fragment_to_template_editor_fragment,
          bundleOf("templateId" to template.id))
      }
      template.apply {
        templateNameTv.text = name
      }
    }
  }

  inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val nameTv: TextView = itemView.dinner_item_name_tv
    private val durationTv: TextView = itemView.dinner_item_duration_tv

    fun init(meal: Meal) {
      itemView.setOnClickListener {
        navController.navigate(R.id.action_management_fragment_to_meal_editor_fragment, bundleOf("mealId" to meal.id))
      }
      meal.apply {
        nameTv.text = name
        durationTv.text = duration.toDurationString()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      ManagementViewModel.Mode.PRODUCT.ordinal -> ProductViewHolder(
        inflater.inflate(R.layout.product_browser_item, parent, false))
      ManagementViewModel.Mode.TEMPLATE.ordinal -> TemplateViewHolder(
        inflater.inflate(R.layout.template_browser_item, parent, false))
      ManagementViewModel.Mode.MEAL.ordinal -> MealViewHolder(
        inflater.inflate(R.layout.meal_browser_item, parent, false))
      else -> throw RuntimeException("Invalid viewType: $viewType")
    }
  }

  override fun getItemViewType(position: Int) = mode.ordinal

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (mode) {
      ManagementViewModel.Mode.PRODUCT -> (holder as ProductViewHolder).init(products[position])
      ManagementViewModel.Mode.TEMPLATE -> (holder as TemplateViewHolder).init(templates[position])
      ManagementViewModel.Mode.MEAL -> (holder as MealViewHolder).init(meals[position])
    }
  }

  override fun getItemCount() = when (mode) {
    ManagementViewModel.Mode.PRODUCT -> products.size
    ManagementViewModel.Mode.TEMPLATE -> templates.size
    ManagementViewModel.Mode.MEAL -> meals.size
  }

  override fun onItemDismiss(position: Int) {
    super.onItemDismiss(position)
    coroutineScope.launch(Dispatchers.IO) {
      when (mode) {
        ManagementViewModel.Mode.PRODUCT -> {
          val product = products[position]
          productRepository.deleteProduct(product.id)
          products.remove(product)
        }
        ManagementViewModel.Mode.TEMPLATE -> {
          val template = templates[position]
          templateRepository.deleteTemplate(template.id)
          templates.remove(template)
        }
        ManagementViewModel.Mode.MEAL -> {
          val meal = meals[position]
          mealRepository.deleteMeal(meal.id)
          meals.remove(meal)
        }
      }
      withContext(Dispatchers.Main) { notifyItemRemoved(position) }
    }
  }

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface ManagementRecyclerAdapterEntryPoint {
    fun getProductRepository(): ProductRepository
    fun getTemplateRepository(): TemplateRepository
    fun getMealRepository(): MealRepository
  }
}