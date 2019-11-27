package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.shopping_category_item.view.*
import kotlinx.android.synthetic.main.shopping_item.view.*

class ShoppingListAdapter(private val context: Context,
                          private val onClick: (holder: ShoppingViewHolder, entry: ShoppingItem) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    private lateinit var shoppingList: MutableList<ShoppingEntry>
    private lateinit var categoryList: List<CategoryEntity>
    private lateinit var shoppingListOrder: Map<Int, Int>
    private var customShoppingListOrder: Boolean = false

    private var adapterList = mutableListOf<ListAdapterItem>()
    var checkedEntries: MutableList<Int> = mutableListOf()
        private set

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val product: TextView = itemView.shopping_item_name_tv
        val amount: TextView = itemView.shopping_item_quantity_input
        val checkbox: CheckBox = itemView.shopping_item_checkbox
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.category_name_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        SHOPPING_ITEM -> ShoppingViewHolder(inflater.inflate(R.layout.shopping_item, parent, false))
        CATEGORY_NAME -> CategoryViewHolder(inflater.inflate(R.layout.shopping_category_item, parent, false))
        else -> throw IllegalArgumentException("Invalid viewType: $viewType!")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            SHOPPING_ITEM -> {
                val holder = holder as ShoppingViewHolder
                val currentEntry = adapterList[position] as ShoppingItem
                holder.product.text = currentEntry.entry.product.name
                holder.amount.text = context.getString(R.string.shopping_entry_amount, currentEntry.entry.amount)
                holder.itemView.setOnClickListener { onClick(holder, currentEntry) }
            }
            CATEGORY_NAME -> {
                val holder = holder as CategoryViewHolder
                val currentEntry = adapterList[position] as CategoryName
                holder.name.text = currentEntry.category.name
            }
            else -> throw java.lang.IllegalArgumentException("Invalid viewType: ${holder.itemViewType}")
        }
    }

    override fun getItemViewType(position: Int): Int = when (adapterList[position]) {
        is ShoppingItem -> SHOPPING_ITEM
        is CategoryName -> CATEGORY_NAME
    }



    override fun getItemCount() = adapterList.size

    internal fun setShoppingList(shoppingList: MutableList<ShoppingEntry>) {
        this.shoppingList = shoppingList
        update()
    }

    internal fun setCategoryList(categoryList: List<CategoryEntity>) {
        this.categoryList = categoryList
        if (!customShoppingListOrder) setShoppingListOrderFromCategoryList()
        update()
    }

    internal fun setCustomShoppingListOrder(shoppingListOrder: Map<Int, Int>) {
        this.shoppingListOrder = shoppingListOrder
        customShoppingListOrder = true
        update()
    }

    internal fun resetCustomShoppingListOrder() {
        setShoppingListOrderFromCategoryList()
        customShoppingListOrder = false
        update()
    }

    private fun setShoppingListOrderFromCategoryList() {
        shoppingListOrder = categoryList
            .associate { Pair(it.id, it.position) }
            .toMutableMap()
    }

    private fun update() {
        if (::shoppingList.isInitialized &&
            ::categoryList.isInitialized &&
            ::shoppingListOrder.isInitialized) {
            adapterList = mutableListOf()
            shoppingList
                .groupBy { it.product.categoryId }
                .toSortedMap(compareBy { categoryId -> shoppingListOrder[categoryId] })
                .forEach {
                    adapterList.add(CategoryName(categoryList.first { categoryListItem -> categoryListItem.id == it.key }))
                    adapterList.addAll(it.value.map { entry -> ShoppingItem(entry) })
                }
            checkedEntries.clear()
            notifyDataSetChanged()
        }
    }

    internal fun getShoppingListOrder(): List<Pair<Int, Int>> = shoppingListOrder.toList()

    companion object {
        private const val SHOPPING_ITEM = 0
        private const val CATEGORY_NAME = 1
    }
}

sealed class ListAdapterItem
data class CategoryName(val category: CategoryEntity) : ListAdapterItem()
data class ShoppingItem(val entry: ShoppingEntry) : ListAdapterItem()