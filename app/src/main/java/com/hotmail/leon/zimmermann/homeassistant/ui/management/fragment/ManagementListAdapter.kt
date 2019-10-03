package com.hotmail.leon.zimmermann.homeassistant.ui.management.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.measure.Measure
import com.hotmail.leon.zimmermann.homeassistant.models.tables.product.ProductEntity
import kotlinx.android.synthetic.main.management_item.view.*

class ManagementListAdapter internal constructor(
    private val context: Context,
    private val onClickListener: View.OnClickListener
) :
    RecyclerView.Adapter<ManagementListAdapter.ProductViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var productList = emptyList<ProductEntity>()

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.name_tv
        val productCapacityView: TextView = itemView.management_item_capacity_tv
        val productQuantityView: TextView = itemView.management_item_quantity_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = inflater.inflate(R.layout.management_item, parent, false)
        itemView.setOnClickListener(onClickListener)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val current = productList[position]
        holder.productNameView.text = current.name
        holder.productCapacityView.text = context.resources.getString(
            R.string.management_item_capacity,
            current.capacity,
            if (current.measureId != null) Measure.values()[current.measureId!!].abbreviation else ""
        )
        holder.productQuantityView.text = context.resources.getString(
            R.string.management_item_quantity,
            current.quantity,
            current.min,
            current.max
        )
    }

    internal fun setProductList(productEntityList: List<ProductEntity>) {
        this.productList = productEntityList
        notifyDataSetChanged()
    }

    operator fun get(position: Int): ProductEntity = productList[position]

    override fun getItemCount(): Int = productList.size
}