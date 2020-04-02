package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.meals.creation

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
import com.hotmail.leon.zimmermann.homeassistant.databinding.DinnerCreationFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.camera.CameraFragment
import kotlinx.android.synthetic.main.dinner_creation_fragment.*
import kotlinx.android.synthetic.main.dinner_creation_fragment.view.*
import java.io.File

class MealCreationFragment : Fragment() {

    private lateinit var viewModel: MealCreationViewModel
    private lateinit var binding: DinnerCreationFragmentBinding
    private lateinit var adapter: MealIngredientsAdapter
    private var photoFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            photoFile = it.getSerializable(FILE) as? File
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dinner_creation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(MealCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = DinnerCreationFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()
        adapter = MealIngredientsAdapter(context!!, ::onIngredientRemoved).apply {
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
        viewModel.mealTemplateList.observe(this, Observer { consumptionEntityList ->
            adapter.setConsumptionEntityList(consumptionEntityList)
            val params = view.add_ingredients_button.layoutParams as ViewGroup.MarginLayoutParams
            val topMarginDimensionId = if (consumptionEntityList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
            params.topMargin = context!!.resources.getDimension(topMarginDimensionId).toInt()
            view.add_ingredients_button.layoutParams = params
        })
        view.ingredients_list.adapter = adapter
        view.ingredients_list.layoutManager = LinearLayoutManager(context)

        photoFile?.let {
            val bitmap = BitmapFactory.decodeStream(it.inputStream())
            consumption_creation_image_view.setImageBitmap(bitmap)
            consumption_creation_image_view.apply {
                imageTintMode = PorterDuff.Mode.DST
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    private fun onIngredientRemoved(position: Int) {
        val consumptionTemplateList = viewModel.mealTemplateList.value!!
        consumptionTemplateList.removeAt(position)
        viewModel.mealTemplateList.value = consumptionTemplateList
    }

    inner class EventHandler {
        fun onImageViewClicked(view: View) {
            photoFile = CameraFragment.createTempPhotoFile(context!!)
            findNavController().navigate(
                R.id.action_dinner_creation_fragment_to_camera_fragment,
                bundleOf("file" to photoFile)
            )
        }

        fun onAddIngredientsButtonClicked(view: View) {
            MealIngredientsDialog().show(fragmentManager!!, "ConsumptionIngredientsDialog")
        }

        fun onSaveDinnerButtonClicked(view: View) {
            viewModel.saveDinnerToDatabase()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (photoFile != null) outState.putSerializable(FILE, photoFile)
    }

    companion object {
        private const val FILE = "file"

        fun newInstance() = MealCreationFragment()
    }
}
