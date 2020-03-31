package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.calendar.activity.dinner

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.CalendarActivityDinnerStepOneFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.DinnerListAdapter
import kotlinx.android.synthetic.main.calendar_activity_dinner_step_one_fragment.*
import kotlinx.android.synthetic.main.dinner_browser_fragment.*

class CalendarActivityDinnerStepOneFragment(
    private val nextStepCallback: () -> Unit
) : Fragment() {

    private lateinit var viewModel: CalendarActivityDinnerViewModel
    private lateinit var binding: CalendarActivityDinnerStepOneFragmentBinding
    private lateinit var adapter: DinnerListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(CalendarActivityDinnerViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.calendar_activity_dinner_step_one_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = CalendarActivityDinnerStepOneFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = DinnerListAdapter(context!!, View.OnClickListener { onItemClicked(it) })
        dinner_selection_list.adapter = adapter
        dinner_selection_list.layoutManager = LinearLayoutManager(context!!)
        viewModel.consumptionLists.observe(this, Observer {
            adapter.setConsumptionLists(it)
        })
    }

    private fun onItemClicked(view: View) {
        viewModel.consumptionList = adapter[dinner_selection_list.getChildAdapterPosition(view)]
        nextStepCallback()
    }
}