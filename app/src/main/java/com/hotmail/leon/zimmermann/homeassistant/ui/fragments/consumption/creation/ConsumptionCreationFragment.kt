package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import android.graphics.BitmapFactory
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
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
import com.hotmail.leon.zimmermann.homeassistant.databinding.ConsumptionCreationFragmentBinding
import com.hotmail.leon.zimmermann.homeassistant.ui.components.recyclerViewHandler.RecyclerViewHandler
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.camera.CameraFragment
import kotlinx.android.synthetic.main.consumption_creation_fragment.*
import kotlinx.android.synthetic.main.consumption_creation_fragment.view.*
import kotlinx.android.synthetic.main.consumption_creation_fragment.view.consumption_creation_image_view
import java.io.File

class ConsumptionCreationFragment : Fragment() {

    private lateinit var viewModel: ConsumptionCreationViewModel
    private lateinit var binding: ConsumptionCreationFragmentBinding
    private lateinit var adapter: ConsumptionIngredientsAdapter

    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            file = it.getSerializable(FILE) as? File
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_creation_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding = ConsumptionCreationFragmentBinding.bind(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.eventHandler = EventHandler()
        adapter = ConsumptionIngredientsAdapter(context!!, ::onIngredientRemoved).apply {
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
        viewModel.consumptionTemplateList.observe(this, Observer { consumptionEntityList ->
            adapter.setConsumptionEntityList(consumptionEntityList)
            val params = view.add_ingredients_button.layoutParams as ViewGroup.MarginLayoutParams
            val topMarginDimensionId = if (consumptionEntityList.isEmpty()) R.dimen.lMargin else R.dimen.sMargin
            params.topMargin = context!!.resources.getDimension(topMarginDimensionId).toInt()
            view.add_ingredients_button.layoutParams = params
        })
        view.ingredients_list.adapter = adapter
        view.ingredients_list.layoutManager = LinearLayoutManager(context)

        file?.let {
            val bitmap = BitmapFactory.decodeStream(it.inputStream())
            consumption_creation_image_view.setImageBitmap(bitmap)
            consumption_creation_image_view.apply {
                imageTintMode = PorterDuff.Mode.DST
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }
    }

    private fun onIngredientRemoved(position: Int) {
        val consumptionTemplateList = viewModel.consumptionTemplateList.value!!
        consumptionTemplateList.removeAt(position)
        viewModel.consumptionTemplateList.value = consumptionTemplateList
    }

    inner class EventHandler {

        fun onImageViewClicked(view: View) {
            file = CameraFragment.createTempPhotoFile(context!!)
            findNavController().navigate(
                R.id.action_consumption_creation_fragment_to_camera_fragment,
                bundleOf("file" to file)
            )
        }

        fun onAddIngredientsButtonClicked(view: View) {
            ConsumptionIngredientsDialog().show(fragmentManager!!, "ConsumptionIngredientsDialog")
        }

        fun onSaveDinnerButtonClicked(view: View) {
            viewModel.saveDinnerToDatabase()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (file != null) outState.putSerializable(FILE, file)
    }

    companion object {
        private const val FILE = "file"

        fun newInstance() = ConsumptionCreationFragment()
    }
}
