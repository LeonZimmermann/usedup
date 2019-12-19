package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.timeline

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityType
import kotlinx.android.synthetic.main.timeline_activity.view.*
import kotlinx.android.synthetic.main.timeline_day.view.*
import org.jetbrains.anko.image
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TimelineAdapter(
    private val context: Context,
    private val onClickListener: (CalendarActivityEntity) -> Unit
) :
    RecyclerView.Adapter<TimelineAdapter.TimelineViewHolder>() {

    private var calendarActivities: List<List<CalendarActivityEntity>> = listOf()
    private val dateFormatter = SimpleDateFormat.getDateInstance()
    private val timeFormatter = SimpleDateFormat.getTimeInstance(DateFormat.SHORT)
    private val weekdayFormatter = SimpleDateFormat("E")

    inner class TimelineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayDateTextView: TextView = itemView.day_date_tv
        val dayWeekdayTextView: TextView = itemView.day_weekday_tv
        val dayActivityContainer: LinearLayout = itemView.day_activity_container
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.timeline_day, parent, false)
        return TimelineViewHolder(view)
    }

    // TODO Remove Constraint
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        val item = calendarActivities[position]
        holder.dayDateTextView.text = dateFormatter.format(item.first().date)
        holder.dayWeekdayTextView.text = weekdayFormatter.format(item.first().date)
        holder.dayActivityContainer.removeAllViews()
        for (calendarActivity in item) {
            val activityView = LayoutInflater.from(context).inflate(R.layout.timeline_activity, null)
            val type = CalendarActivityType.values()[calendarActivity.typeId]
            activityView.activity_time_tv.text = timeFormatter.format(calendarActivity.date)
            activityView.activity_icon_image.image = context.resources.getDrawable(type.icon, null)
            activityView.activity_name_tv.text = type.name
            activityView.setOnClickListener { onClickListener(calendarActivity) }
            holder.dayActivityContainer.addView(activityView)
        }
    }

    internal fun setCalendarActivities(calendarActivities: List<CalendarActivityEntity>) {
        this.calendarActivities = calendarActivities.asSequence()
            .groupBy { getDayOfDate(it.date) }
            .toList()
            .sortedBy { it.first }
            .map { it.second }
            .map { list -> list.sortedBy { it.date.time } }
            .toList()
        notifyDataSetChanged()
    }

    private fun getDayOfDate(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR] + calendar[Calendar.DAY_OF_YEAR]
    }

    override fun getItemCount(): Int = calendarActivities.size

}