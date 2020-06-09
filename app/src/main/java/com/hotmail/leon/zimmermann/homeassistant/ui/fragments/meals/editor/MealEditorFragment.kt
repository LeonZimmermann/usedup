package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.editor

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
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.camera.CameraFragment
import kotlinx.android.synthetic.main.meal_editor_fragment.*
import kotlinx.android.synthetic.main.meal_editor_fragment.view.*
import java.io.File

class MealEditorFragment : Fragment() {

    private lateinit var viewModel: MealEditorViewModel
    private lateinit var binding: MealEditorFragmentBinding
    private lateinit var adapter: MealTemplateAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            viewModel.photoFile = it.getSerializable(FILE) as? File
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.meal_editor_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MealEditorViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        initDatabinding(view)
        adapter = MealTemplateAdapter(context!!, ::onIngredientRemoved).apply {
            ItemTouchHelper(object : RecyclerViewHandler(this) {
                override fun getMovementFlags(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                    return makeMovementFlags(0x00, swipeFlags)
                }
            }).attachToRecyclerView(view.ingredients_list)
        }
        viewModel.mealTemplateList.observe(viewLifecycleOwner, Observer { consumptionEntityList ->
            adapter.setConsumptionEntityList(consumptionEntityList)
            val params = view.add_ingredients_button.layoutParams as ViewGroup.MarginLayoutParams
            val topMarginDimensionId = if (consumptionEntityList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
            params.topMargin = context!!.resources.getDimension(topMarginDimensionId).toInt()
            view.add_ingredients_button.layoutParams = params
        })
        view.ingredients_list.adapter = adapter
        view.ingredients_list.layoutManager = LinearLayoutManager(context)

        viewModel.photoFile?.let {
            val bitmap = BitmapFactory.decodeStream(it.inputStream())
            consumption_creation_image_view.setImageBitmap(bitmap)
            consumption_creation_image_view.apply {
                imageTintMode = PorterDuff.Mode.DST
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    private fun initDatabinding(view: View) {
        binding = MealEditorFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()
    }

    private fun onIngredientRemoved(position: Int) {
        val consumptionTemplateList = viewModel.mealTemplateList.value!!
        consumptionTemplateList.removeAt(position)
        viewModel.mealTemplateList.value = consumptionTemplateList
    }

    inner class EventHandler {
        fun onImageViewClicked(view: View) {
            viewModel.photoFile = CameraFragment.createTempPhotoFile(context!!)
            findNavController().navigate(
                R.id.action_meal_editor_fragment_to_camera_fragment,
                bundleOf("file" to viewModel.photoFile)
            )
        }

        fun onAddIngredientsButtonClicked(view: View) {
            MealIngredientsDialog().show(fragmentManager!!, "ConsumptionIngredientsDialog")
        }

        fun onSaveDinnerButtonClicked(view: View) {
            viewModel.addNewMealToDatabase()
            findNavController().navigateUp()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (viewModel.photoFile != null) outState.putSerializable(FILE, viewModel.photoFile)
    }

    companion object {
        private const val FILE = "file"

        fun newInstance() = MealEditorFragment()
    }
}
