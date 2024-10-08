package de.usedup.android.management

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.usedup.android.R
import de.usedup.android.components.recyclerViewHandler.RecyclerViewHandler
import de.usedup.android.datamodel.api.objects.Meal
import de.usedup.android.datamodel.api.objects.Product
import de.usedup.android.datamodel.api.objects.Template
import de.usedup.android.datamodel.api.repositories.MealRepository
import de.usedup.android.datamodel.api.repositories.TemplateRepository
import de.usedup.android.datamodel.api.repositories.product.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ManagementViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
  productRepository: ProductRepository,
  templateRepository: TemplateRepository,
  mealRepository: MealRepository
) : ViewModel(), TabLayout.OnTabSelectedListener {

  val products: LiveData<Set<Product>> = productRepository.getAllProductsLiveData(viewModelScope)
  val templates: LiveData<Set<Template>> = templateRepository.getAllTemplates(viewModelScope)
  val meals: LiveData<Set<Meal>> = mealRepository.getAllMeals(viewModelScope)

  val noEntries: MutableLiveData<Boolean> = MutableLiveData()
  val noEntryMessage: MutableLiveData<String> = MutableLiveData()

  var mode: MutableLiveData<Mode> = MutableLiveData(Mode.PRODUCT)

  lateinit var adapter: ManagementRecyclerAdapter

  fun initAdapter(recyclerView: RecyclerView) {
    adapter = ManagementRecyclerAdapter(context, recyclerView, Navigation.findNavController(recyclerView),
      viewModelScope).apply {
      ItemTouchHelper(object : RecyclerViewHandler(this) {
        override fun getMovementFlags(
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder
        ): Int {
          val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
          return makeMovementFlags(0x00, swipeFlags)
        }
      }).attachToRecyclerView(recyclerView)
    }
  }

  fun onAddButtonClicked(view: View) {
    when (adapter.mode) {
      Mode.PRODUCT -> Navigation.findNavController(view)
        .navigate(R.id.action_management_fragment_to_product_editor_fragment)
      Mode.TEMPLATE -> Navigation.findNavController(view)
        .navigate(R.id.action_management_fragment_to_template_editor_fragment)
      Mode.MEAL -> Navigation.findNavController(view)
        .navigate(R.id.action_management_fragment_to_meal_editor_fragment)
    }
  }

  override fun onTabReselected(tab: TabLayout.Tab) {}
  override fun onTabUnselected(tab: TabLayout.Tab) {}
  override fun onTabSelected(tab: TabLayout.Tab) {
    when (tab.position) {
      0 -> {
        mode.postValue(Mode.PRODUCT)
        noEntryMessage.postValue(context.getString(R.string.no_products_yet_message))
        noEntries.postValue(products.value?.isEmpty() ?: true)
      }
      1 -> {
        mode.postValue(Mode.TEMPLATE)
        noEntryMessage.postValue(context.getString(R.string.no_templates_yet_message))
        noEntries.postValue(templates.value?.isEmpty() ?: true)
      }
      2 -> {
        mode.postValue(Mode.MEAL)
        noEntryMessage.postValue(context.getString(R.string.no_meals_yet_message))
        noEntries.postValue(meals.value?.isEmpty() ?: true)
      }
    }
  }

  enum class Mode {
    PRODUCT, TEMPLATE, MEAL
  }
}