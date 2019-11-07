package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.creation

import android.app.Activity.RESULT_OK
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionCreationFragmentBinding

class ConsumptionCreationFragment : Fragment() {

    private val pictureHandler = ConsumptionCreationPictureHandler(this)

    private lateinit var viewModel: ConsumptionCreationViewModel
    private lateinit var binding: ConsumptionCreationFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_creation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        val binding = ConsumptionCreationFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
            pictureHandler.handleTakePictureResult()
    }

    inner class EventHandler {

        fun onIngredientsButtonClicked(view: View) {
            findNavController().navigate(R.id.action_consumption_creation_fragment_to_ingredients_fragment)
        }

        fun onDescriptionButtonClicked(view: View) {
            findNavController().navigate(
                R.id.action_consumption_creation_fragment_to_text_input_fragment,
                bundleOf("requestMode" to REQUEST_DESCRIPTION_TEXT)
            )
        }

        fun onInstructionsButtonClicked(view: View) {
            findNavController().navigate(
                R.id.action_consumption_creation_fragment_to_text_input_fragment,
                bundleOf("requestMode" to REQUEST_INSTRUCTIONS_TEXT)
            )
        }

        fun onImageViewClicked(view: View) {
            pictureHandler.dispatchTakePictureIntent()
        }
    }

    companion object {
        fun newInstance() = ConsumptionCreationFragment()

        const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_DESCRIPTION_TEXT = 2
        const val REQUEST_INSTRUCTIONS_TEXT = 3
    }
}
