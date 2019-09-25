package com.hotmail.leon.zimmermann.homeassistant.fragments.transaction

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.product.ProductEntity
import kotlinx.android.synthetic.main.product_item.view.*

class TransactionBatchListAdapter internal constructor(context: Context) :
    RecyclerView.Adapter<TransactionBatchListAdapter.TransactionBatchViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var transactionList = mutableListOf<Pair<ProductEntity, Int>>()

    inner class TransactionBatchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productNameView: TextView = itemView.product_name_tv
        val countView: TextView = itemView.count_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionBatchViewHolder {
        return TransactionBatchViewHolder(inflater.inflate(R.layout.product_item, parent, false))
    }

    override fun getItemCount() = transactionList.size

    override fun onBindViewHolder(holder: TransactionBatchViewHolder, position: Int) {
        val (product, quantity) = transactionList[position]
        holder.productNameView.text = product.name
        holder.countView.text = quantity.toString()
    }

    internal fun setTransactionList(transactionList: MutableList<Pair<ProductEntity, Int>>) {
        this.transactionList = transactionList
        notifyDataSetChanged()
    }
}