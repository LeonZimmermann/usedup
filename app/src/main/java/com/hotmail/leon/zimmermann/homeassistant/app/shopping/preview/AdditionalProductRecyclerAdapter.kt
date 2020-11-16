package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_preview_shopping_entry.view.*

class AdditionalProductRecyclerAdapter(context: Context) :
  RecyclerView.Adapter<AdditionalProductRecyclerAdapter.AdditionalProductViewHolder>() {

  private var inflater = LayoutInflater.from(context)
  private var additionalProductList: List<AdditionalProductRepresentation> = listOf()

  inner class AdditionalProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val productNameTextView: TextView = itemView.product_name_tv
    private val amountTextView: TextView = itemView.amount_tv

    fun init(additionalProduct: AdditionalProductRepresentation) {
      additionalProduct.apply {
        productNameTextView.text = nameString
        amountTextView.text = discrepancyString
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
}