package com.hotmail.leon.zimmermann.homeassistant.ui.components.picker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.datamodel.Meal
import kotlinx.android.synthetic.main.meal_browser_item.view.*

class DinnerListAdapter(context: Context, private val  onClickListener: View.OnClickListener) :
    RecyclerView.Adapter<DinnerListAdapter.DinnerViewHolder>() {

    private val inflater = LayoutInflater.from(context)
    private var mealList = emptyList<Meal>()

    inner class DinnerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameView: TextView = itemView.dinner_item_name_tv
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DinnerViewHolder {
        val itemView = inflater.inflate(R.layout.meal_browser_item, parent, false)
        itemView.setOnClickListener(onClickListener)
        return DinnerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DinnerViewHolder, position: Int) {
        val current = mealList[position]
        holder.nameView.text = current.name
    }

    internal fun setMealList(mealList: List<Meal>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }

    internal operator fun get(position: Int) = mealList[position]

    override fun getItemCount(): Int = mealList.size
}