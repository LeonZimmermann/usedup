package de.usedup.android.management.meals

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.camera.CameraFragment
import de.usedup.android.components.consumption.ConsumptionElement
import de.usedup.android.components.consumption.ConsumptionElementAdapter
import de.usedup.android.components.consumption.ConsumptionElementDialogFragment
import de.usedup.android.components.recyclerViewHandler.RecyclerViewHandler
import de.usedup.android.databinding.MealEditorFragmentBinding
import de.usedup.android.datamodel.api.objects.Id
import kotlinx.android.synthetic.main.meal_editor_fragment.*
import kotlinx.android.synthetic.main.meal_editor_fragment.view.*

@AndroidEntryPoint
class MealEditorFragment : Fragment() {

  private val viewModel: MealEditorViewModel by viewModels()
  private lateinit var binding: MealEditorFragmentBinding
  private lateinit var adapter: ConsumptionElementAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    (savedInstanceState?.getSerializable(MEAL_ID) as? Id?
      ?: arguments?.getSerializable(MEAL_ID) as? Id?)
      ?.let { viewModel.setMealId(it) }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.meal_editor_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initBackStackHandler()
    initImageHandler()
    initAddIngredientsClickedHandler()
    initOpenCameraHandler()
    initNavigateUpHandler()
    initIngredientsAdapter()
    initErrorMessageHandler()
  }

  private fun initDatabinding() {
    binding = MealEditorFragmentBinding.bind(requireView())
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initBackStackHandler() {
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Bitmap>(CameraFragment.BITMAP)
      ?.observe(viewLifecycleOwner) { bitmap -> viewModel.setImageFromBitmap(bitmap) }
  }

  private fun initImageHandler() {
    viewModel.image.observe(viewLifecycleOwner) { image ->
      Glide.with(requireView())
        .load(image)
        .into(consumption_creation_image_view)
      consumption_creation_image_view.apply {
        imageTintMode = PorterDuff.Mode.DST
        scaleType = ImageView.ScaleType.FIT_CENTER
      }
    }
  }

  private fun initAddIngredientsClickedHandler() {
    viewModel.addIngredientsClicked.observe(viewLifecycleOwner) { addIngredientsClicked ->
      if (addIngredientsClicked) {
        ConsumptionElementDialogFragment(object : ConsumptionElementDialogFragment.Callback {
          override fun onPositiveButtonClicked(consumptionElement: ConsumptionElement) {
            viewModel.addConsumptionElement(consumptionElement)
          }
        }).show(parentFragmentManager, "ConsumptionElementDialog")
        viewModel.addIngredientsClicked.postValue(false)
      }
    }
  }

  private fun initOpenCameraHandler() {
    viewModel.openCamera.observe(viewLifecycleOwner) { openCamera ->
      if (openCamera) {
        findNavController().navigate(R.id.action_meal_editor_fragment_to_camera_fragment)
        viewModel.openCamera.postValue(false)
      }
    }
  }

  private fun initNavigateUpHandler() {
    viewModel.navigateUp.observe(viewLifecycleOwner) { navigateUp ->
      if (navigateUp) {
        findNavController().navigateUp()
        viewModel.navigateUp.postValue(false)
      }
    }
  }

  private fun initErrorMessageHandler() {
    viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
      errorMessage?.let { Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show() }
    }
  }

  private fun initIngredientsAdapter() {
    adapter = ConsumptionElementAdapter(requireContext(), ::onIngredientRemoved).apply {
      ItemTouchHelper(object : RecyclerViewHandler(this) {
        override fun getMovementFlags(
          recyclerView: RecyclerView,
          viewHolder: RecyclerView.ViewHolder
        ): Int {
          val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
          return makeMovementFlags(0x00, swipeFlags)
        }
      }).attachToRecyclerView(requireView().ingredients_list)
    }
    requireView().ingredients_list.adapter = adapter
    requireView().ingredients_list.layoutManager = LinearLayoutManager(context)
    viewModel.consumptionElementList.observe(viewLifecycleOwner, { consumptionElementList ->
      adapter.setConsumptionElementList(consumptionElementList)
      val params = requireView().add_ingredients_button.layoutParams as ViewGroup.MarginLayoutParams
      val topMarginDimensionId = if (consumptionElementList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
      params.topMargin = requireContext().resources.getDimension(topMarginDimensionId).toInt()
      requireView().add_ingredients_button.layoutParams = params
    })
  }

  private fun onIngredientRemoved(position: Int) {
    val consumptionTemplateList = requireNotNull(viewModel.consumptionElementList.value)
    consumptionTemplateList.removeAt(position)
    viewModel.consumptionElementList.postValue(consumptionTemplateList)
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel.mealId?.let { outState.putSerializable(MEAL_ID, it) }
  }

  companion object {
    const val MEAL_ID = "mealId"

    fun newInstance() = MealEditorFragment()
  }
}
