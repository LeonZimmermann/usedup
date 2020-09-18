package com.hotmail.leon.zimmermann.homeassistant.app.ui.meals.browser

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.MealBrowserFragmentBinding

class MealBrowserFragment : Fragment() {

    private lateinit var viewModel: MealBrowserViewModel
    private lateinit var binding: MealBrowserFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MealBrowserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.meal_browser_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabinding(view)
        initList()
    }

    private fun initDatabinding(view: View) {
        binding = MealBrowserFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun initList() {
        /*
        adapter = com.hotmail.leon.zimmermann.homeassistant.components.picker.DinnerListAdapter(
            context!!,
            View.OnClickListener { onItemClicked(it) })
        consumption_browser_list.adapter = adapter
        consumption_browser_list.layoutManager = LinearLayoutManager(context!!)
        // TODO Init List
         */
    }

    private fun onItemClicked(view: View) {
        // TODO Implement
    }

    companion object {
        fun newInstance() = MealBrowserFragment()
    }
}
