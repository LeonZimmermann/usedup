package de.usedup.android.shopping.preview

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.usedup.android.R
import kotlinx.android.synthetic.main.shopping_list_preview_product.view.*

class ProductDiscrepancyRecyclerAdapter(context: Context, var callback: Callback) :
  RecyclerView.Adapter<ProductDiscrepancyRecyclerAdapter.ProductDiscrepancyViewHolder>() {

  private var inflater = LayoutInflater.from(context)
  private var productDiscrepancyList: MutableList<ProductDiscrepancyRepresentation> = mutableListOf()

  inner class ProductDiscrepancyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val productNameTextView: TextView = itemView.product_name_tv
    private val amountTextView: TextView = itemView.amount_tv
    private val editButton: ImageView = itemView.edit_button
    private val removeButton: ImageView = itemView.remove_button

    fun init(index: Int, productDiscrepancy: ProductDiscrepancyRepresentation) {
      productDiscrepancy.apply {
        productNameTextView.text = nameString
        amountTextView.text = discrepancyString
        editButton.setOnClickListener {
          callback.onEditButtonClicked(this@ProductDiscrepancyRecyclerAdapter, index, itemView, productDiscrepancy)
        }
        removeButton.setOnClickListener {
          callback.onRemoveButtonClicked(this@ProductDiscrepancyRecyclerAdapter, index, itemView, productDiscrepancy)
        }
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductDiscrepancyViewHolder {
    return ProductDiscrepancyViewHolder(inflater.inflate(R.layout.shopping_list_preview_product, parent, false))
  }

  override fun onBindViewHolder(holder: ProductDiscrepancyViewHolder, position: Int) {
    holder.init(position, productDiscrepancyList[position])
  }

  override fun getItemCount(): Int = productDiscrepancyList.size

  internal fun initProductDiscrepancyList(productDiscrepancyList: List<ProductDiscrepancyRepresentation>) {
    this.productDiscrepancyList = productDiscrepancyList.toMutableList()
    notifyDataSetChanged()
  }

  internal fun addProductDiscrepancy(productDiscrepancy: ProductDiscrepancyRepresentation) {
    Log.d(TAG, "addProductDiscrepancy: $productDiscrepancy")
    this.productDiscrepancyList.add(productDiscrepancy)
    notifyItemInserted(this.productDiscrepancyList.size - 1)
  }

  internal fun replaceProductDiscrepancy(index: Int, productDiscrepancy: ProductDiscrepancyRepresentation) {
    Log.d(TAG, "replaceProductDiscrepancy: $index, $productDiscrepancy")
    this.productDiscrepancyList[index] = productDiscrepancy
    notifyItemChanged(index)
  }

  internal fun removeProductDiscrepancy(index: Int) {
    Log.d(TAG, "removeProductDiscrepancy: $index")
    this.productDiscrepancyList.removeAt(index)
    notifyItemRemoved(index)
    for (i in index until itemCount) {
      notifyItemChanged(i)
    }
  }

  interface Callback {
    fun onEditButtonClicked(adapter: ProductDiscrepancyRecyclerAdapter, index: Int, view: View,
      productDiscrepancyRepresentation: ProductDiscrepancyRepresentation)

    fun onRemoveButtonClicked(adapter: ProductDiscrepancyRecyclerAdapter, index: Int, view: View,
      productDiscrepancyRepresentation: ProductDiscrepancyRepresentation)
  }

  companion object {
    private const val TAG = "PDRA"
  }
}