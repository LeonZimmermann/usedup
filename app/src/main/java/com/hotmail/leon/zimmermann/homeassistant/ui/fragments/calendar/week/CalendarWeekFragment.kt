package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.week

import android.graphics.RectF
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.CalendarWeekView
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.CalendarViewModel
import kotlinx.android.synthetic.main.calendar_week_fragment.*

class CalendarWeekFragment : Fragment() {

    private lateinit var viewModel: CalendarViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CalendarViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_week_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        calendar_week.onDateTimeSelectedListener = object : CalendarWeekView.OnDateTimeSelectedListener {
            override fun onDateTimeSelected(weekday: Int, hour: Int, minute: Int) {
                Toast.makeText(
                    context,
                    "DateTime selected: $weekday - $hour:$minute",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        calendar_week.onEntryClickedListener = object : CalendarWeekView.OnEntryClickedListener {
            override fun onEntryClicked(entry: CalendarWeekView.Entry, rect: RectF, index: Int) {
                Toast.makeText(
                    context,
                    "Entry clicked: $entry $rect $index",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.calendarActivities.observe(this, Observer { calendarActivities ->
            calendar_week.setEntryList(calendarActivities.map {
                CalendarWeekView.Entry(it.dateFrom, it.dateTo)
            }.toMutableList())
        })
        add_button.setOnClickListener {
            findNavController().navigate(R.id.action_global_calendar_activity_dinner_fragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.calendar_menu, menu)
        menu.removeItem(R.id.week_option)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.timeline_option -> {
            findNavController().navigate(R.id.action_calendar_week_fragment_to_timeline_fragment)
            true
        }
        else -> false
    }

    companion object {
        fun newInstance() = CalendarWeekFragment()
    }
}
