package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ProductEditorFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.datamodel.exceptions.InvalidInputException
import kotlinx.android.synthetic.main.product_editor_fragment.*

class ProductEditorFragment : Fragment() {

    private lateinit var viewModel: ProductEditorViewModel
    private lateinit var binding: ProductEditorFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        initProductId(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.product_editor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDatabinding(view)
        initCategoryInput()
        initMeasureInput()
        initSaveButton(view)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders.of(this)[ProductEditorViewModel::class.java]
    }

    private fun initProductId(savedInstanceState: Bundle?) {
        arguments?.apply {
            val productId = getSerializable(PRODUCT_ID) as? String?
            // TODO Implement Callbacks
            productId?.let { viewModel.setProductId(it, {}, {}) }
        }
        savedInstanceState?.apply {
            val productId = getSerializable(PRODUCT_ID) as? String?
            // TODO Implement Callbacks
            productId?.let { viewModel.setProductId(it, {}, {}) }
        }
    }

    private fun initCategoryInput() {
        category_input.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_item,
                viewModel.categoryList.map { it.name })
        )
    }

    private fun initMeasureInput() {
        measure_input.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.list_item,
                viewModel.measureList.map { it.name })
        )
    }

    private fun initSaveButton(view: View) {
        save_button.setOnClickListener {
            try {
                viewModel.save(category_input.text.toString(), measure_input.text.toString())
                findNavController().navigateUp()
            } catch (e: InvalidInputException) {
                e.message?.let { Snackbar.make(view, it, Snackbar.LENGTH_LONG).show()  }
            }
        }
    }

    private fun initDatabinding(view: View) {
        binding = ProductEditorFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.buttonText = if (viewModel.productId == null)
            resources.getString(R.string.add_product) else resources.getString(R.string.update_product)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(PRODUCT_ID, viewModel.productId)
    }

    companion object {
        private const val PRODUCT_ID = "productId"

        fun newInstance() = ProductEditorFragment()
    }
}
