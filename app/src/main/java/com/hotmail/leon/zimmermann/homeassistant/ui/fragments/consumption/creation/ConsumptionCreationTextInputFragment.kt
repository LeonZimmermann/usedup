package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.text_input_fragment.*
import java.lang.RuntimeException

class ConsumptionCreationTextInputFragment : Fragment() {

    companion object {
        fun newInstance() =
            ConsumptionCreationTextInputFragment()
    }

    private lateinit var viewModel: ConsumptionCreationViewModel
    private var requestMode: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.text_input_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestMode = arguments!!.getInt("requestMode")
        applyOnSelectedField { field -> field.observe(this, Observer<String> { text_input.setText(it) }) }
    }

    private fun applyOnSelectedField(function: (field: MutableLiveData<String>) -> Unit) = when (requestMode) {
        ConsumptionCreationFragment.REQUEST_DESCRIPTION_TEXT -> function(viewModel.descriptionString)
        ConsumptionCreationFragment.REQUEST_INSTRUCTIONS_TEXT -> function(viewModel.instructionsString)
        else -> throw RuntimeException("Invalid requestMode supplied! (requestMode: $requestMode)")
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.text_input_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.delete_option -> {
            applyOnSelectedField { field -> field.value = null }
            findNavController().navigateUp()
            true
        }
        R.id.submit_option -> {
            applyOnSelectedField { field -> field.value = text_input.text.toString() }
            findNavController().navigateUp()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}
