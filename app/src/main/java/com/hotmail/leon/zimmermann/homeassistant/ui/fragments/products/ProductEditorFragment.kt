package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ProductEditorFragmentBinding
import kotlinx.android.synthetic.main.product_editor_fragment.*

class ProductEditorFragment : Fragment() {

    private lateinit var viewModel: ProductEditorViewModel
    private lateinit var binding: ProductEditorFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[ProductEditorViewModel::class.java]
        arguments?.apply {
            viewModel.productId = getSerializable(PRODUCT_ID) as? Long?
        }
        savedInstanceState?.apply {
            viewModel.productId = getSerializable(PRODUCT_ID) as? Long?
        }
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
        viewModel.categoryList.observe(viewLifecycleOwner, Observer { categoryList ->
            category_input.adapter =
                ArrayAdapter(context!!, android.R.layout.simple_list_item_1, categoryList.map { it.name })
        })
        viewModel.measureList.observe(viewLifecycleOwner, Observer { measureList ->
            measure_input.adapter =
                ArrayAdapter(context!!, android.R.layout.simple_list_item_1, measureList.map { it.text })
        })
    }

    private fun initDatabinding(view: View) {
        binding = ProductEditorFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
