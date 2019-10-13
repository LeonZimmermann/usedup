package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.details

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hotmail.leon.zimmermann.homeassistant.R

class ConsumptionDetailsFragment : Fragment() {

    companion object {
        fun newInstance() = ConsumptionDetailsFragment()
    }

    private lateinit var viewModel: ConsumptionDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consumption_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ConsumptionDetailsViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
