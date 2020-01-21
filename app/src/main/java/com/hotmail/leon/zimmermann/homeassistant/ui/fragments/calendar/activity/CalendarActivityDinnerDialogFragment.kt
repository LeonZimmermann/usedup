package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.CalendarActivityDinnerFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityEntity
import com.hotmail.leon.zimmermann.homeassistant.models.tables.calendar.CalendarActivityType
import com.hotmail.leon.zimmermann.homeassistant.models.tables.consumption.ConsumptionList
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.DatePickerFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.TimePickerFragment
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.browser.ConsumptionBrowserFragment
import kotlinx.android.synthetic.main.calendar_activity_dinner_fragment.*
import org.jetbrains.anko.support.v4.toast
import java.sql.Date
import java.util.*

class CalendarActivityDinnerDialogFragment : Fragment() {

    private lateinit var viewModel: CalendarActivityDinnerViewModel
    private lateinit var binding: CalendarActivityDinnerFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CalendarActivityDinnerViewModel::class.java)
        arguments?.apply {
            (getSerializable("dinner") as ConsumptionList?)?.let { viewModel.consumptionList = it }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_activity_dinner_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarActivityDinnerFragmentBinding.bind(view)
        initDatabinding()
        initDateInput()
        initCookTimeInput()
        initAddButton()
        dinner_input.setOnClickListener { navigateToBrowser() }
        dinner_card.setOnClickListener { navigateToBrowser() }
        viewModel.consumptionList?.let { initDinner(it) }
    }

    private fun initDatabinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initDateInput() {
        date_input.setOnClickListener {
            DatePickerFragment(object : DatePickerFragment.OnDateSetListener {
                @SuppressLint("SetTextI18n") // TODO Remove
                override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                    val calendar = Calendar.getInstance()
                    calendar.time = viewModel.date.value!!
                    calendar[Calendar.YEAR] = year
                    calendar[Calendar.MONTH] = month
                    calendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                    viewModel.date.value = calendar.time
                }
            }).show(fragmentManager!!, "DatePicker")
        }
    }

    private fun initCookTimeInput() {
        cook_time_input.setOnClickListener {
            TimePickerFragment(object : TimePickerFragment.OnTimeSetListener {
                @SuppressLint("SetTextI18n") // TODO Remove
                override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
                    val calendar = Calendar.getInstance()
                    calendar.time = viewModel.date.value!!
                    calendar[Calendar.HOUR_OF_DAY] = hourOfDay
                    calendar[Calendar.MINUTE] = minute
                    viewModel.date.value = calendar.time
                }
            }).show(fragmentManager!!, "TimePicker")
        }
    }

    private fun initDinner(consumptionList: ConsumptionList) {
        consumption_list_name_tv.text = consumptionList.metaData.name
        consumption_list_duration_tv.text = consumptionList.metaData.duration.toString()
        consumption_list_short_description_tv.text = consumptionList.metaData.description
    }

    private fun initAddButton() {
        add_button.setOnClickListener {
            try {
                if (viewModel.consumptionList == null) throw IllegalArgumentException("Select a Dinner")
                val duration = viewModel.consumptionList!!.metaData.duration ?: 0
                val time = viewModel.date.value?.time ?: throw IllegalArgumentException("Invalid Date")
                val dateFrom = Date(time)
                val dateTo = Date(time + 1000 * 60 * duration)
                viewModel.insertCalendarActivity(CalendarActivityEntity(
                    dateFrom,
                    dateTo,
                    CalendarActivityType.COOKING.id,
                    viewModel.consumptionList!!.metaData.id
                ))
                findNavController().navigateUp()
            } catch (e: IllegalArgumentException) {
                e.message?.let { toast(it) }
            }
        }
    }

    private fun navigateToBrowser() {
        findNavController().navigate(
            R.id.action_global_consumption_browser_fragment,
            bundleOf("mode" to ConsumptionBrowserFragment.Mode.SELECT)
        )
    }

    companion object {
        fun newInstance() = CalendarActivityDinnerDialogFragment()
    }
}