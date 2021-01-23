package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_preview_product.view.*
import kotlin.math.log

class AdditionalProductRecyclerAdapter(context: Context, var callback: Callback) :
  RecyclerView.Adapter<AdditionalProductRecyclerAdapter.AdditionalProductViewHolder>() {

  private var inflater = LayoutInflater.from(context)
  private var additionalProductList: MutableList<AdditionalProductRepresentation> = mutableListOf()

  inner class AdditionalProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productNameTextView: TextView = itemView.product_name_tv
    val amountTextView: TextView = itemView.amount_tv
    val editButton: ImageView = itemView.edit_button
    val removeButton: ImageView = itemView.remove_button
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdditionalProductViewHolder {
    return AdditionalProductViewHolder(inflater.inflate(R.layout.shopping_list_preview_product, parent, false))
  }

  override fun onBindViewHolder(holder: AdditionalProductViewHolder, position: Int) {
    additionalProductList[position].apply {
      holder.productNameTextView.text = nameString
      holder.amountTextView.text = discrepancyString
      holder.editButton.setOnClickListener {
        callback.onEditButtonClicked(this@AdditionalProductRecyclerAdapter, position, holder.itemView, this)
      }
      holder.removeButton.setOnClickListener {
        callback.onRemoveButtonClicked(this@AdditionalProductRecyclerAdapter, position, holder.itemView, this)
      }
    }
  }

  override fun getItemCount(): Int = additionalProductList.size

  internal fun initAdditionalProductList(additionalProductList: List<AdditionalProductRepresentation>) {
    this.additionalProductList = additionalProductList.toMutableList()
    notifyDataSetChanged()
  }

  internal fun addAdditionalProduct(additionalProductRepresentation: AdditionalProductRepresentation) {
    this.additionalProductList.add(additionalProductRepresentation)
    notifyItemInserted(this.additionalProductList.size - 1)
  }

  internal fun replaceAdditionalProduct(additionalProductRepresentation: AdditionalProductRepresentation) {
    val index = this.additionalProductList.indexOfFirst { it.data == additionalProductRepresentation.data }
    this.additionalProductList[index] = additionalProductRepresentation
    notifyItemChanged(index)
  }

  internal fun removeAdditionalProduct(index: Int) {
    this.additionalProductList.removeAt(index)
    notifyItemRemoved(index)
    for (i in index until itemCount) {
      notifyItemChanged(i)
    }
  }

  interface Callback {
    fun onEditButtonClicked(adapter: AdditionalProductRecyclerAdapter, index: Int, view: View,
      additionalProductRepresentation: AdditionalProductRepresentation)

    fun onRemoveButtonClicked(adapter: AdditionalProductRecyclerAdapter, index: Int, view: View,
      additionalProductRepresentation: AdditionalProductRepresentation)
  }

  companion object {
    private const val TAG = "APRA"
  }
}