package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner.CalendarActivityDinnerStepOneFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner.CalendarActivityDinnerStepTwoFragment

class CalendarActivityFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CalendarActivityDinnerStepOneFragment()
        1 -> CalendarActivityDinnerStepTwoFragment()
        else -> throw RuntimeException("Invalid position: $position")
    }
}