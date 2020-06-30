package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.objects.Category
import kotlinx.android.synthetic.main.shopping_category.view.*
import org.jetbrains.anko.textView

class ShoppingListAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private var categories: List<Category> = listOf()
    private var shoppingList: Map<Category, List<ShoppingProduct>> = mapOf()

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shoppingCategoryNameTextView: TextView = itemView.shopping_category_name_tv
        val shoppingProductListContainer: LinearLayout = itemView.shopping_product_list_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListAdapter.ShoppingViewHolder =
        ShoppingViewHolder(LayoutInflater.from(context).inflate(R.layout.shopping_category, parent, false))

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val category = categories[position]
        val products = shoppingList[category]
        holder.shoppingCategoryNameTextView.text = category.name
        products?.let { products ->
            holder.shoppingProductListContainer.removeAllViews()
            holder.shoppingProductListContainer.apply {
                for (item in products) {
                    textView {
                        setOnClickListener {
                            paintFlags = paintFlags xor Paint.STRIKE_THRU_TEXT_FLAG
                            item.checked = !item.checked
                        }
                        text = "${item.cartAmount}x ${item.product.name}"
                        textSize = 22f
                    }
                }
            }
        }
    }

    override fun getItemCount() = shoppingList.size

    internal fun setData(categories: List<Category>, shoppingList: Map<Category, List<ShoppingProduct>>) {
        this.categories = categories
        this.shoppingList = shoppingList
        notifyDataSetChanged()
    }
}