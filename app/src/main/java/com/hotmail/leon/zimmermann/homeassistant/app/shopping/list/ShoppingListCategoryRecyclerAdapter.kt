package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_category.view.*

class ShoppingListCategoryRecyclerAdapter(private val context: Context, private val callback: ShoppingListElementRecyclerAdapter.Callback) :
  RecyclerView.Adapter<ShoppingListCategoryRecyclerAdapter.ShoppingListCategoryViewHolder>() {

  private val layoutInflater = LayoutInflater.from(context)

  private var shoppingListCategories: List<ShoppingListCategoryRepresentation> = listOf()

  inner class ShoppingListCategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val categoryNameTextView: TextView = itemView.category_name_tv
    val shoppingListCategoryRecyclerAdapter: RecyclerView = itemView.shopping_list_category_recycler_view
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListCategoryViewHolder {
    return ShoppingListCategoryViewHolder(layoutInflater.inflate(R.layout.shopping_list_category, parent, false))
  }

  override fun onBindViewHolder(holder: ShoppingListCategoryViewHolder, position: Int) {
    val shoppingListCategory = shoppingListCategories[position]
    holder.categoryNameTextView.text = shoppingListCategory.name
    val adapter = ShoppingListElementRecyclerAdapter(context, callback)
    holder.shoppingListCategoryRecyclerAdapter.layoutManager = LinearLayoutManager(context)
    holder.shoppingListCategoryRecyclerAdapter.adapter = adapter
    adapter.initShoppingListElements(shoppingListCategory.shoppingListElementRepresentation)
  }

  override fun getItemCount(): Int = shoppingListCategories.size

  internal fun initShoppingListCategories(shoppingListCategories: Set<ShoppingListCategoryRepresentation>) {
    this.shoppingListCategories = shoppingListCategories.toList()
    notifyDataSetChanged()
  }
}