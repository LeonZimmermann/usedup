package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.week

import android.graphics.RectF
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.CalendarWeekView
import kotlinx.android.synthetic.main.calendar_week_fragment.*
import java.util.*

class CalendarWeekFragment : Fragment() {

    private lateinit var viewModel: CalendarWeekViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewModel = ViewModelProviders.of(this).get(CalendarWeekViewModel::class.java)
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
        val random = Random()
        calendar_week.setEntryList(MutableList(20) { index ->
            val calendar = Calendar.getInstance()
            calendar[Calendar.DAY_OF_WEEK] = random.nextInt(7)
            calendar[Calendar.HOUR_OF_DAY] = random.nextInt(23)
            calendar[Calendar.MINUTE] = random.nextInt(2) * 30
            val fromDate = calendar.time
            calendar[Calendar.HOUR_OF_DAY] += 1
            val toDate = calendar.time
            CalendarWeekView.Entry(fromDate, toDate)
        })
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
