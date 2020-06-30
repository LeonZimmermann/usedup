package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.management

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
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.MealRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Product
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Template
import kotlinx.android.synthetic.main.meal_browser_item.view.*
import kotlinx.android.synthetic.main.product_browser_item.view.*
import kotlinx.android.synthetic.main.template_browser_item.view.*

class ManagementRecyclerAdapter constructor(context: Context, private val navController: NavController) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    var products: List<Pair<String, Product>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var templates: List<Pair<String, Template>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var meals: List<Pair<String, Meal>> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var mode: ManagementFragment.Mode = ManagementFragment.Mode.PRODUCT
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv: TextView = itemView.name_tv
        private val managementItemQuantityTv: TextView = itemView.management_item_quantity_tv
        private val managementItemCapacityTv: TextView = itemView.management_item_capacity_tv

        fun init(data: Pair<String, Product>) {
            itemView.setOnClickListener {
                navController.navigate(
                    R.id.action_management_fragment_to_product_editor_fragment, bundleOf(
                        "productId" to data.first
                    )
                )
            }
            data.second.apply {
                nameTv.text = name
                managementItemQuantityTv.text = quantity.toString()
                managementItemCapacityTv.text = capacity.toString()
            }
        }
    }

    inner class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val templateNameTv: TextView = itemView.template_name

        fun init(data: Pair<String, Template>) {
            itemView.setOnClickListener {
                navController.navigate(
                    R.id.action_management_fragment_to_template_editor_fragment, bundleOf(
                        "templateId" to data.first
                    )
                )
            }
            data.second.apply {
                templateNameTv.text = name
            }
        }
    }

    inner class MealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTv: TextView = itemView.dinner_item_name_tv
        private val durationTv: TextView = itemView.dinner_item_duration_tv
        private val shortDescriptionTv: TextView = itemView.dinner_item_short_description_tv

        fun init(data: Pair<String, Meal>) {
            itemView.setOnClickListener {
                navController.navigate(
                    R.id.action_management_fragment_to_meal_editor_fragment, bundleOf(
                        "mealId" to data.first
                    )
                )
            }
            data.second.apply {
                nameTv.text = name
                durationTv.text = duration.toString()
                shortDescriptionTv.text = description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ManagementFragment.Mode.PRODUCT.ordinal -> ProductViewHolder(inflater.inflate(R.layout.product_browser_item, parent, false))
            ManagementFragment.Mode.TEMPLATE.ordinal -> TemplateViewHolder(inflater.inflate(R.layout.template_browser_item, parent, false))
            ManagementFragment.Mode.MEAL.ordinal -> MealViewHolder(inflater.inflate(R.layout.meal_browser_item, parent, false))
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
}