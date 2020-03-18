package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.calendar_activity_dialog_fragment.*


class CalendarActivityDinnerFragment : Fragment() {

    private lateinit var viewModel: CalendarActivityDinnerViewModel
    private lateinit var calendarActivityDinnerFragmentStateAdapter: CalendarActivityDinnerFragmentStateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CalendarActivityDinnerViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

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
        calendarActivityDinnerFragmentStateAdapter =
            CalendarActivityDinnerFragmentStateAdapter(
                this,
                ::scrollToNextStep
            )
        pager.adapter = calendarActivityDinnerFragmentStateAdapter
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                indicator.selection = position
            }
        })

        pager.isUserInputEnabled = false
    }

    private fun scrollToStep(step: Int) {
        pager.postDelayed({
            pager.currentItem = step
        }, 1)
    }

    private fun scrollToNextStep() {
        pager.postDelayed({
            pager.currentItem += 1
        }, 1)
    }

    companion object {
        fun newInstance() =
            CalendarActivityDinnerFragment()
    }
}