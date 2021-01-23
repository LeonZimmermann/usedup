package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_element.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.textColor

class ShoppingListElementRecyclerAdapter(private val context: Context, private val callback: Callback) :
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
    if (shoppingListElement.checked) {
      holder.productNameTextView.textColor = context.getColor(R.color.textColorLight)
      holder.checkButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.negative))
      holder.checkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cross_icon))
    } else {
      // TODO Set to the correct color
      holder.productNameTextView.textColor = context.getColor(android.R.color.black)
      holder.checkButton.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary))
      holder.checkButton.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.check_icon))
    }
    holder.checkButton.setOnClickListener {
      shoppingListElement.checked = !shoppingListElement.checked
      notifyItemChanged(position)
      callback.onCheckButtonPressed(shoppingListElement)
    }
  }

  override fun getItemCount(): Int = shoppingListElements.size

  internal fun initShoppingListElements(shoppingListElements: Set<ShoppingListElementRepresentation>) {
    this.shoppingListElements = shoppingListElements.toList()
    notifyDataSetChanged()
  }

  interface Callback {
    fun onCheckButtonPressed(shoppingListElementRepresentation: ShoppingListElementRepresentation)
  }
}