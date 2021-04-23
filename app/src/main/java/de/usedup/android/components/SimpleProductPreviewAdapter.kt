package de.usedup.android.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.Product
import kotlinx.android.synthetic.main.product_item.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView

class SimpleProductPreviewAdapter constructor(
  context: Context,
  private val recyclerView: RecyclerView,
  private val onClickListener: View.OnClickListener? = null
) :
  RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private val inflater = LayoutInflater.from(context)
  var productAmountList = emptyList<Pair<Product, Int>>()
    set(value) {
      field = value
      notifyDataSetChanged()
      recyclerView.scheduleLayoutAnimation()
    }
  var additionalAmount: Int = 0
    set(value) {
      field = value
      notifyDataSetChanged()
      recyclerView.scheduleLayoutAnimation()
    }

  inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val productNameView: TextView = itemView.product_name_tv
    val amountTv: TextView = itemView.amount_tv
  }

  inner class AdditionalMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  override fun getItemViewType(position: Int): Int =
    if (position >= productAmountList.size) TYPE_ADDITIONAL_MESSAGE else TYPE_PRODUCT

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      TYPE_PRODUCT -> createProductViewHolder(parent)
      TYPE_ADDITIONAL_MESSAGE -> createAdditionalMessageViewHolder(parent)
      else -> throw RuntimeException("Invalid viewType")
    }
  }

  private fun createProductViewHolder(parent: ViewGroup): ProductViewHolder {
    val view = inflater.inflate(R.layout.product_item, parent, false)
    if (onClickListener != null) view.setOnClickListener(onClickListener)
    return ProductViewHolder(view)
  }

  private fun createAdditionalMessageViewHolder(parent: ViewGroup): AdditionalMessageViewHolder {
    val view = LinearLayout(parent.context).apply {
      setPadding(0, dip(10f), 0, dip(10f))
      textView {
        text = context.resources.getString(R.string.more_discrepancy_message, additionalAmount)
        textColor = context.resources.getColor(android.R.color.darker_gray, null)
      }
    }
    return AdditionalMessageViewHolder(view)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    when (holder.itemViewType) {
      TYPE_PRODUCT -> {
        val current = productAmountList[position]
        (holder as ProductViewHolder).productNameView.text = current.first.name
        holder.amountTv.text = current.second.toString()
      }
    }
  }

  override fun getItemCount() = productAmountList.size + (if (additionalAmount > 0) 1 else 0)
  operator fun get(index: Int) = productAmountList[index]

  companion object {
    private const val TYPE_PRODUCT = 0
    private const val TYPE_ADDITIONAL_MESSAGE = 1
  }
}