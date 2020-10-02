package com.hotmail.leon.zimmermann.homeassistant.app.calendar

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.CalendarTimelineFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.databinding.CalendarWeekFragmentBinding
import kotlinx.android.synthetic.main.calendar_week_fragment.*

class CalendarWeekFragment : Fragment() {

  private lateinit var viewModel: CalendarViewModel
  private lateinit var binding: CalendarWeekFragmentBinding

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initViewModel()
    setHasOptionsMenu(true)
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.calendar_week_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    initCalendarWeekView()
    initToastMessage()
  }

  private fun initViewModel() {
    viewModel = activity?.run {
      ViewModelProviders.of(this).get(CalendarViewModel::class.java)
    } ?: throw Exception("Invalid Activity")
  }

  private fun initDatabinding(view: View) {
    binding = CalendarWeekFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initCalendarWeekView() {
    calendar_week.onDateTimeSelectedListener = viewModel
    calendar_week.onEntryClickedListener = viewModel
    calendar_week.onScrollListener = viewModel
  }

  private fun initToastMessage() {
    viewModel.toastMessage.observe(viewLifecycleOwner, Observer { toastMessage ->
      Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
    })
  }

  override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
    inflater.inflate(R.menu.calendar_menu, menu)
    menu.removeItem(R.id.week_option)
    return super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
    R.id.timeline_option -> {
      findNavController().navigate(R.id.action_calendar_week_fragment_to_calendar_timeline_fragment)
      true
    }
    else -> false
  }

  companion object {
    fun newInstance() = CalendarWeekFragment()
  }
}
