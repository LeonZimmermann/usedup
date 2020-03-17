package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.CategoryEntity
import kotlinx.android.synthetic.main.shopping_category.view.*
import org.jetbrains.anko.textView

class ShoppingListAdapter(
    private val context: Context,
    itemTouchHelperReceiver: ShoppingListAdapter.() -> ItemTouchHelper,
    private val productLongClickCallback: (value: Int, editCallback: (Int) -> Unit, deleteCallback: () -> Unit) -> Unit
) : RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private var shoppingList: MutableMap<Int, MutableList<ShoppingProduct>> = mutableMapOf()
    private var shoppingListOrder: MutableMap<Int, CategoryEntity> = mutableMapOf()
    private var itemTouchHelper = itemTouchHelperReceiver()
    private lateinit var categories: List<CategoryEntity>

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shoppingCategoryNameTextView: TextView = itemView.shopping_category_name_tv
        val shoppingProductListContainer: LinearLayout = itemView.shopping_product_list_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ShoppingViewHolder =
        ShoppingViewHolder(LayoutInflater.from(context).inflate(R.layout.shopping_category, parent, false))

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val category =
            shoppingListOrder[position] ?: throw RuntimeException("CategoryEntity was not found in shoppingListOrder!")
        val products = shoppingList[category.id]
        holder.shoppingCategoryNameTextView.text = category.name
        holder.shoppingCategoryNameTextView.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) itemTouchHelper.startDrag(holder)
            false
        }
        products?.let { products ->
            holder.shoppingProductListContainer.removeAllViews()
            holder.shoppingProductListContainer.apply {
                for (item in products) {
                    textView {
                        setOnClickListener {
                            paintFlags = paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
                            item.checked = !item.checked
                        }
                        setOnLongClickListener {
                            productLongClickCallback(item.cartAmount, {
                                item.cartAmount = it
                                notifyDataSetChanged()
                            }, {
                                products.remove(item)
                                if (products.isEmpty()) shoppingList.remove(category.id)
                                notifyDataSetChanged()
                            })
                            true
                        }
                        text = "${item.cartAmount}x ${item.product.name}"
                        textSize = 22f
                    }
                }
            }
        }
    }

    override fun getItemCount() = shoppingList.size

    internal fun setShoppingList(shoppingList: MutableMap<Int, MutableList<ShoppingProduct>>) {
        this.shoppingList = shoppingList
        updateShoppingListOrder()
        notifyDataSetChanged()
    }

    internal fun setCategories(categories: List<CategoryEntity>) {
        this.categories = categories
        updateShoppingListOrder()
        notifyDataSetChanged()
    }

    private fun updateShoppingListOrder() {
        if (::categories.isInitialized) {
            this.shoppingListOrder = categories
                .filter { shoppingList.keys.contains(it.id) }
                .sortedBy { it.position }
                .mapIndexed { index, categoryEntity -> index to categoryEntity }
                .toMap()
                .toMutableMap()
        }
    }

    inner class ShoppingListItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            return makeMovementFlags(dragFlags, 0x00)
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            if (target.adapterPosition != -1) {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                val tempTargetCategory = shoppingListOrder[toPosition]!!
                shoppingListOrder[toPosition] = shoppingListOrder[fromPosition]!!
                shoppingListOrder[fromPosition] = tempTargetCategory
                notifyItemMoved(fromPosition, toPosition)
                return true
            }
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }
}