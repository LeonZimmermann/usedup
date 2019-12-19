package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.timeline

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.timeline_fragment.*
import org.jetbrains.anko.support.v4.toast

class TimelineFragment : Fragment() {

    companion object {
        fun newInstance() =
            TimelineFragment()
    }

    private lateinit var viewModel: TimelineViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.timeline_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TimelineViewModel::class.java)
        val adapter =
            TimelineAdapter(
                context!!
            ) {
                toast(it.toString())
            }
        timeline_container.adapter = adapter
        timeline_container.layoutManager = LinearLayoutManager(context)
        viewModel.calendarActivities.observe(this, Observer { calendarActivities ->
            adapter.setCalendarActivities(calendarActivities)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.calendar_menu, menu)
        menu.removeItem(R.id.timeline_option)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.week_option -> {
            findNavController().navigate(R.id.action_timeline_fragment_to_calendar_week_fragment)
            true
        }
        else -> false
    }
}
