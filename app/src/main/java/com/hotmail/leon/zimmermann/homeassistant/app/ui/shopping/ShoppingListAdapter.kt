package com.hotmail.leon.zimmermann.homeassistant.app.ui.shopping

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
import com.hotmail.leon.zimmermann.homeassistant.datamodel.repositories.CategoryRepository
import kotlinx.android.synthetic.main.shopping_category.view.*
import org.jetbrains.anko.textView

class ShoppingListAdapter(private val context: Context) :
    RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>() {

    private var shoppingList: List<Pair<Category, List<ShoppingProduct>>> = listOf()

    inner class ShoppingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val shoppingCategoryNameTextView: TextView = itemView.shopping_category_name_tv
        val shoppingProductListContainer: LinearLayout = itemView.shopping_product_list_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingViewHolder =
        ShoppingViewHolder(LayoutInflater.from(context).inflate(R.layout.shopping_category, parent, false))

    override fun onBindViewHolder(holder: ShoppingViewHolder, position: Int) {
        val (category, products) = shoppingList[position]
        holder.shoppingCategoryNameTextView.text = category.name
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

    override fun getItemCount() = shoppingList.size

    internal fun setData(shoppingList: List<ShoppingProduct>) {
        this.shoppingList = shoppingList
            .groupBy { CategoryRepository.getCategoryForId(it.product.categoryId) }
            .toList()
        notifyDataSetChanged()
    }
}