package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner.CalendarActivityDinnerStepOneFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner.CalendarActivityDinnerStepThreeFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner.CalendarActivityDinnerStepTwoFragment

class CalendarActivityFragmentStateAdapter(
    fragment: Fragment,
    private val nextStepCallback: () -> Unit
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> CalendarActivityDinnerStepOneFragment(nextStepCallback)
        1 -> CalendarActivityDinnerStepTwoFragment(nextStepCallback)
        2 -> CalendarActivityDinnerStepThreeFragment()
        else -> throw RuntimeException("Invalid position: $position")
    }
}