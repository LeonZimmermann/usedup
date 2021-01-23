package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_element.view.*

class ShoppingListElementRecyclerAdapter(private val context: Context) :
  RecyclerView.Adapter<ShoppingListElementRecyclerAdapter.ShoppingListElementViewHolder>() {

  private val layoutInflater = LayoutInflater.from(context)

  private var shoppingListElements: List<ShoppingListElementRepresentation> = listOf()

  inner class ShoppingListElementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productNameTextView: TextView = itemView.product_name_tv
    val amountTextView: TextView = itemView.amount_tv
    val checkButton: ImageView = itemView.check_button
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingListElementViewHolder {
    return ShoppingListElementViewHolder(layoutInflater.inflate(R.layout.shopping_list_element, parent, false))
  }

  override fun onBindViewHolder(holder: ShoppingListElementViewHolder, position: Int) {
    val shoppingListElement = shoppingListElements[position]
    holder.productNameTextView.text = shoppingListElement.product.name
    holder.amountTextView.text = shoppingListElement.cartAmount.toString()
    holder.checkButton.setOnClickListener {
      // TODO Implement
    }
  }

  override fun getItemCount(): Int = shoppingListElements.size

  internal fun initShoppingListElements(shoppingListElements: Set<ShoppingListElementRepresentation>) {
    this.shoppingListElements = shoppingListElements.toList()
    notifyDataSetChanged()
  }
}