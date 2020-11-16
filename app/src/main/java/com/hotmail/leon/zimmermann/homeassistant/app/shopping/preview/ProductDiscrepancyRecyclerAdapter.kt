package com.hotmail.leon.zimmermann.homeassistant.app.shopping.preview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.shopping_list_preview_shopping_entry.view.*

class ProductDiscrepancyRecyclerAdapter(context: Context) :
  RecyclerView.Adapter<ProductDiscrepancyRecyclerAdapter.ProductDiscrepancyViewHolder>() {

  private var inflater = LayoutInflater.from(context)
  private var productDiscrepancyList: List<ProductDiscrepancyRepresentation> = listOf()

  inner class ProductDiscrepancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val productNameTextView: TextView = itemView.product_name_tv
    private val amountTextView: TextView = itemView.amount_tv

    fun init(productDiscrepancy: ProductDiscrepancyRepresentation) {
      productDiscrepancy.apply {
        productNameTextView.text = nameString
        amountTextView.text = discrepancyString
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDiscrepancyViewHolder {
    return ProductDiscrepancyViewHolder(inflater.inflate(R.layout.shopping_list_preview_shopping_entry, parent, false))
  }

  override fun onBindViewHolder(holder: ProductDiscrepancyViewHolder, position: Int) {
    holder.init(productDiscrepancyList[position])
  }

  override fun getItemCount(): Int = productDiscrepancyList.size

  internal fun setProductDiscrepancyList(productDiscrepancyList: List<ProductDiscrepancyRepresentation>) {
    this.productDiscrepancyList = productDiscrepancyList
    notifyDataSetChanged()
  }
}