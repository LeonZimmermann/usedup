package com.hotmail.leon.zimmermann.homeassistant.ui.components

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.product_item.view.*

class SimpleProductPreviewAdapter constructor(context: Context, private val onClickListener: View.OnClickListener? = null) :
    RecyclerView.Adapter<SimpleProductPreviewAdapter.ProductViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    var productAmountList = emptyList<Pair<ProductEntity, Int>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.product_name_tv
        val amountTv: TextView = itemView.amount_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = inflater.inflate(R.layout.product_item, parent, false)
        if (onClickListener != null) view.setOnClickListener(onClickListener)
        return ProductViewHolder(view)
    }

    override fun getItemCount() = productAmountList.size
    operator fun get(index: Int) = productAmountList[index]

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = productAmountList[position]
        holder.productNameView.text = current.first.name
        holder.amountTv.text = current.second.toString()
    }


}