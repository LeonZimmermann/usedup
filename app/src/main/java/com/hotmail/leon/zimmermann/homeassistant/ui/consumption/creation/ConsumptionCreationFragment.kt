package com.hotmail.leon.zimmermann.homeassistant.ui.consumption.creation

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.consumption_creation_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ConsumptionCreationFragment : Fragment() {

    companion object {
        fun newInstance() = ConsumptionCreationFragment()

        private const val REQUEST_IMAGE_CAPTURE = 1
        const val REQUEST_DESCRIPTION_TEXT = 2
        const val REQUEST_INSTRUCTIONS_TEXT = 3
    }

    private lateinit var viewModel: ConsumptionCreationViewModel
    private var currentPhotoPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ConsumptionCreationViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_creation_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.descriptionString.observe(this, androidx.lifecycle.Observer {
            consumption_creation_description_tv.text = it
        })
        viewModel.instructionsString.observe(this, androidx.lifecycle.Observer {
            consumption_creation_instructions_tv.text = it
        })
        consumption_creation_description_button.setOnClickListener {
            findNavController().navigate(
                R.id.action_consumption_creation_fragment_to_text_input_fragment,
                bundleOf("requestMode" to REQUEST_DESCRIPTION_TEXT)
            )
        }
        consumption_creation_instructions_button.setOnClickListener {
            findNavController().navigate(
                R.id.action_consumption_creation_fragment_to_text_input_fragment,
                bundleOf("requestMode" to REQUEST_INSTRUCTIONS_TEXT)
            )
        }
        consumption_creation_image_view.setOnClickListener { dispatchTakePictureIntent() }

    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                try {
                    val photoFile = createImageFile()
                    photoFile.also {
                        val photoURI: Uri =
                            FileProvider.getUriForFile(
                                context!!,
                                "com.hotmail.leon.zimmermann.homeassistant.fileprovider",
                                it
                            )
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                    }
                } catch (exception: IOException) {
                    val message = "Could not create image file"
                    Toast.makeText(context!!, message, Toast.LENGTH_LONG).show()
                    Log.e(
                        "ConsumptionCreation",
                        "dispatchTakePictureIntent: $message (Exception: ${exception.message}"
                    )
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Glide.with(context!!)
                .load(currentPhotoPath)
                .into(consumption_creation_image_view)
            consumption_creation_image_view.apply {
                scaleType = ImageView.ScaleType.CENTER
                imageTintList = null
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply { currentPhotoPath = absolutePath }
    }

}
