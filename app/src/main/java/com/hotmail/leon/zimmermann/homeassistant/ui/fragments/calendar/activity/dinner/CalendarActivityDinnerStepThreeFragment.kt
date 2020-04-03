package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.CalendarActivityDinnerStepThreeFragmentBinding
import kotlinx.android.synthetic.main.calendar_activity_dinner_step_three_fragment.*
import kotlinx.android.synthetic.main.dinner_browser_item.view.*

class CalendarActivityDinnerStepThreeFragment : Fragment() {

    private lateinit var viewModel: CalendarActivityDinnerViewModel
    private lateinit var binding: CalendarActivityDinnerStepThreeFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CalendarActivityDinnerViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_activity_dinner_step_three_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarActivityDinnerStepThreeFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.meal!!.apply {
            view.dinner_item_name_tv.text = name
            view.dinner_item_duration_tv.text = duration.toString()
            view.dinner_item_short_description_tv.text = description
        }
        activity_dinner_submit_button.setOnClickListener {
            viewModel.insertCalendarActivity()
            findNavController().navigateUp()
        }
    }
}
