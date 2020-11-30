package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_preview_shopping_entry.view.*

class AdditionalProductRecyclerAdapter(context: Context, var callback: Callback) :
  RecyclerView.Adapter<AdditionalProductRecyclerAdapter.AdditionalProductViewHolder>() {

  private var inflater = LayoutInflater.from(context)
  private var additionalProductList: List<AdditionalProductRepresentation> = listOf()

  inner class AdditionalProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val productNameTextView: TextView = itemView.product_name_tv
    private val amountTextView: TextView = itemView.amount_tv
    private val editButton: ImageView = itemView.edit_button
    private val removeButton: ImageView = itemView.remove_button

    fun init(additionalProduct: AdditionalProductRepresentation) {
      additionalProduct.apply {
        productNameTextView.text = nameString
        amountTextView.text = discrepancyString
        editButton.setOnClickListener { callback.onEditButtonClicked(itemView, additionalProduct) }
        removeButton.setOnClickListener { callback.onRemoveButtonClicked(itemView, additionalProduct) }
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditionalProductViewHolder {
    return AdditionalProductViewHolder(inflater.inflate(R.layout.shopping_list_preview_shopping_entry, parent, false))
  }

  override fun onBindViewHolder(holder: AdditionalProductViewHolder, position: Int) {
    holder.init(additionalProductList[position])
  }

  override fun getItemCount(): Int = additionalProductList.size

  internal fun setAdditionalProductList(additionalProductList: List<AdditionalProductRepresentation>) {
    this.additionalProductList = additionalProductList
    notifyDataSetChanged()
  }

  interface Callback {
    fun onEditButtonClicked(view: View, additionalProductRepresentation: AdditionalProductRepresentation)
    fun onRemoveButtonClicked(view: View, additionalProductRepresentation: AdditionalProductRepresentation)
  }
}