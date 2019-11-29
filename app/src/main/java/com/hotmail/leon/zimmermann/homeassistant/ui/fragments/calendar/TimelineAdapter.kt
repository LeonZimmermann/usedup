package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.timeline_day.view.*

class TimelineAdapter(private val context: Context, private val list: List<Any>) :
    RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayDateTextView: TextView = itemView.day_date_tv
        val dayWeekdayTextView: TextView = itemView.day_weekday_tv
        val dayActivityContainer: LinearLayout = itemView.day_activity_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.timeline_day, parent, false)
        return TimelineViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        for (i in 1..3) holder.dayActivityContainer.addView(
            LayoutInflater.from(context).inflate(
                R.layout.timeline_activity,
                null
            )
        )
    }

    override fun getItemCount(): Int = list.size
}