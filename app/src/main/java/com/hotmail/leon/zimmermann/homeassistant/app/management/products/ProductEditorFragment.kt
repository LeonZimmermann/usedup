package com.hotmail.leon.zimmermann.homeassistant.app.management.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.ProductEditorFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.product_editor_fragment.*

@AndroidEntryPoint
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
    initSystemMessage()
  }

  private fun initViewModel() {
    viewModel = ViewModelProviders.of(this)[ProductEditorViewModel::class.java]
  }

  private fun initProductId(savedInstanceState: Bundle?) {
    val productId = (savedInstanceState?.getSerializable(PRODUCT_ID) as? Id?)
      ?: (arguments?.getSerializable(PRODUCT_ID) as? Id?)
    productId?.let { viewModel.setProductId(it) }
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

  private fun initSystemMessage() {
    viewModel.systemMessage.observe(viewLifecycleOwner, Observer { systemMessage ->
        if (!systemMessage.isNullOrBlank()) Snackbar.make(requireView(), systemMessage, Snackbar.LENGTH_LONG).show()
    })
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
