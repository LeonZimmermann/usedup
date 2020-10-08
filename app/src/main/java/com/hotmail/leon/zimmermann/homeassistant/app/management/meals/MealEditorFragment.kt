package com.hotmail.leon.zimmermann.homeassistant.app.management.meals

import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.MealEditorFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.app.camera.CameraFragment
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElementAdapter
import com.hotmail.leon.zimmermann.homeassistant.components.consumption.ConsumptionElementDialogFragment
import com.hotmail.leon.zimmermann.homeassistant.components.recyclerViewHandler.RecyclerViewHandler
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.meal_editor_fragment.*
import kotlinx.android.synthetic.main.meal_editor_fragment.view.*
import java.io.File

@AndroidEntryPoint
class MealEditorFragment : Fragment() {

  private lateinit var viewModel: MealEditorViewModel
  private lateinit var binding: MealEditorFragmentBinding
  private lateinit var adapter: ConsumptionElementAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    initViewModel()
    initMealIdAndPhotoFile(savedInstanceState)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.meal_editor_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding(view)
    initAdapter(view)
    initIngredientsList(view)
    initImageView()
  }

  private fun initViewModel() {
    viewModel = activity?.run {
      ViewModelProviders.of(this).get(MealEditorViewModel::class.java)
    } ?: throw Exception("Invalid Activity")
  }

  private fun initMealIdAndPhotoFile(savedInstanceState: Bundle?) {
    arguments?.apply {
      val mealId = getSerializable(MEAL_ID) as? Id?
      mealId?.let { viewModel.setMealId(it) }
    }
    savedInstanceState?.apply {
      viewModel.photoFile = getSerializable(FILE) as? File
      val mealId = getSerializable(MEAL_ID) as? Id?
      mealId?.let { viewModel.setMealId(it) }
    }
  }

  private fun initDatabinding(view: View) {
    binding = MealEditorFragmentBinding.bind(view)
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
    binding.eventHandler = EventHandler()
  }

  private fun initAdapter(view: View) {
    adapter = ConsumptionElementAdapter(requireContext(), ::onIngredientRemoved).apply {
      ItemTouchHelper(object :
          RecyclerViewHandler(this) {
          override fun getMovementFlags(
              recyclerView: RecyclerView,
              viewHolder: RecyclerView.ViewHolder
          ): Int {
              val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
              return makeMovementFlags(0x00, swipeFlags)
          }
      }).attachToRecyclerView(view.ingredients_list)
    }
    viewModel.consumptionElementList.observe(viewLifecycleOwner, Observer { consumptionElementList ->
        adapter.setConsumptionElementList(consumptionElementList)
        val params = view.add_ingredients_button.layoutParams as ViewGroup.MarginLayoutParams
        val topMarginDimensionId = if (consumptionElementList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
        params.topMargin = requireContext().resources.getDimension(topMarginDimensionId).toInt()
        view.add_ingredients_button.layoutParams = params
    })
  }

  private fun initIngredientsList(view: View) {
    view.ingredients_list.adapter = adapter
    view.ingredients_list.layoutManager = LinearLayoutManager(context)
  }

  private fun initImageView() {
    viewModel.photoFile?.let {
      val bitmap = BitmapFactory.decodeStream(it.inputStream())
      consumption_creation_image_view.setImageBitmap(bitmap)
      consumption_creation_image_view.apply {
        imageTintMode = PorterDuff.Mode.DST
        scaleType = ImageView.ScaleType.FIT_CENTER
      }
    }
  }

  private fun onIngredientRemoved(position: Int) {
    val consumptionTemplateList = viewModel.consumptionElementList.value!!
    consumptionTemplateList.removeAt(position)
    viewModel.consumptionElementList.value = consumptionTemplateList
  }

  inner class EventHandler {
    fun onImageViewClicked(view: View) {
      if (viewModel.photoFile == null) viewModel.photoFile = CameraFragment.createPhotoFile(context!!)
      findNavController().navigate(
          R.id.action_meal_editor_fragment_to_camera_fragment,
          bundleOf("file" to viewModel.photoFile)
      )
    }

    fun onAddIngredientsButtonClicked(view: View) {
      ConsumptionElementDialogFragment().setCallback { viewModel.addConsumptionElement(it) }.show(parentFragmentManager,
          "ConsumptionElementDialog")
    }

    fun onSaveDinnerButtonClicked(view: View) {
      viewModel.addNewMealToDatabase()
      findNavController().navigateUp()
    }
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    viewModel.photoFile?.let { outState.putSerializable(FILE, it) }
    viewModel.mealId?.let { outState.putSerializable(MEAL_ID, it) }
  }

  companion object {
    private const val FILE = "file"
    const val MEAL_ID = "mealId"

    fun newInstance() = MealEditorFragment()
  }
}
