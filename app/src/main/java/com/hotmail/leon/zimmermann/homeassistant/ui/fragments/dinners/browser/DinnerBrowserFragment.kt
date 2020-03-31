package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.dinners.browser

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.DinnerBrowserFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.picker.DinnerListAdapter
import kotlinx.android.synthetic.main.dinner_browser_fragment.*

class DinnerBrowserFragment : Fragment() {

    private lateinit var viewModel: DinnerBrowserViewModel
    private lateinit var binding: DinnerBrowserFragmentBinding
    private lateinit var adapter: DinnerListAdapter
    private lateinit var mode: Mode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(DinnerBrowserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dinner_browser_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DinnerBrowserFragmentBinding.bind(view)
        initArguments()
        initDatabinding()
        initList()
        initAddButton()
    }

    private fun initArguments() {
        arguments?.let {
            mode = it.getSerializable("mode") as Mode
        }
    }

    private fun initDatabinding() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.mode = mode
    }

    private fun initList() {
        adapter = DinnerListAdapter(
            context!!,
            View.OnClickListener { onItemClicked(it) })
        consumption_browser_list.adapter = adapter
        consumption_browser_list.layoutManager = LinearLayoutManager(context!!)
        viewModel.consumptionLists.observe(this, Observer {
            adapter.setConsumptionLists(it)
        })
    }

    private fun initAddButton() {
        consumption_browser_add_button.setOnClickListener {
            findNavController().navigate(R.id.action_consumption_browser_fragment_to_consumption_creation_fragment)
        }
    }

    private fun onItemClicked(view: View) {
        when (mode) {
            Mode.SELECT -> {
                findNavController().popBackStack(R.id.consumption_browser_fragment, true)
                findNavController().navigate(R.id.action_global_calendar_activity_dialog_fragment,
                    bundleOf("dinner" to adapter[consumption_browser_list.getChildAdapterPosition(view)]))
            }
            Mode.EDIT -> findNavController().navigate(
                R.id.action_consumption_browser_fragment_to_consumption_details,
                bundleOf("editConsumptionList" to adapter[consumption_browser_list.getChildAdapterPosition(view)])
            )
        }
    }

    companion object {
        fun newInstance() = DinnerBrowserFragment()
    }

    enum class Mode {
        SELECT,
        EDIT
    }
}
