package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.calendar_activity_dialog_fragment.*


class CalendarActivityFragment : Fragment() {

    private lateinit var viewModel: CalendarActivityViewModel
    private lateinit var calendarActivityFragmentStateAdapter: CalendarActivityFragmentStateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_activity_dialog_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initPager()
    }

    private fun initPager() {
        calendarActivityFragmentStateAdapter = CalendarActivityFragmentStateAdapter(this)
        pager.adapter = calendarActivityFragmentStateAdapter
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.selection = position
            }
        })
    }

    /*
    pager.postDelayed({
            pager.currentItem += 1
        }, 0)
     */

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CalendarActivityViewModel::class.java)
    }

    companion object {
        fun newInstance() = CalendarActivityFragment()
    }
}