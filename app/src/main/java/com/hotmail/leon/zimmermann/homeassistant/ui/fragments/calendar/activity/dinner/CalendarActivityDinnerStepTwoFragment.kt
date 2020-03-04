package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.CalendarActivityDinnerStepTwoFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.DatePickerFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.TimePickerFragment
import kotlinx.android.synthetic.main.calendar_activity_dinner_step_two_fragment.*
import java.util.*

class CalendarActivityDinnerStepTwoFragment : Fragment() {

    private lateinit var viewModel: CalendarActivityDinnerViewModel
    private lateinit var binding: CalendarActivityDinnerStepTwoFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CalendarActivityDinnerViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_activity_dinner_step_two_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarActivityDinnerStepTwoFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        dinner_activity_date_tv.setOnClickListener { showDatePickerDialog() }
        dinner_activity_time_tv.setOnClickListener { showTimePickerDialog() }
    }

    private fun showDatePickerDialog() {
        DatePickerFragment(object: DatePickerFragment.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                viewModel.date.value = Calendar.getInstance().apply {
                    time = viewModel.date.value!!
                    this[Calendar.YEAR] = year
                    this[Calendar.MONTH] = month
                    this[Calendar.DAY_OF_MONTH] = dayOfMonth
                }.time
            }
        }).show(requireFragmentManager(), "DATE_PICKER")
    }

    private fun showTimePickerDialog() {
        TimePickerFragment(object: TimePickerFragment.OnTimeSetListener {
            override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                viewModel.date.value = Calendar.getInstance().apply {
                    time = viewModel.date.value!!
                    this[Calendar.HOUR_OF_DAY] = hourOfDay
                    this[Calendar.MINUTE] = minute
                }.time
            }
        }).show(requireFragmentManager(), "TIME_PICKER")
    }
}
