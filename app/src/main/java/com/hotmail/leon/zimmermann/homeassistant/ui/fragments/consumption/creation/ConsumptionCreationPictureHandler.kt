package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.consumption_creation_fragment.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ConsumptionCreationPictureHandler(private val consumptionCreationFragment: ConsumptionCreationFragment) {
    private var currentPhotoPath: String? = null

    fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            intent.resolveActivity(consumptionCreationFragment.context!!.packageManager)?.also {
                try {
                    val file = createImageFile()
                    val photoURI: Uri =
                        FileProvider.getUriForFile(consumptionCreationFragment.context!!, FILE_PROVIDER, file)
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    consumptionCreationFragment.startActivityForResult(
                        intent,
                        ConsumptionCreationFragment.REQUEST_IMAGE_CAPTURE
                    )
                } catch (exception: IOException) {
                    showToastAndLogErrorMessage("Could not create image file", exception)
                }
            }
        }
    }

    private fun showToastAndLogErrorMessage(message: String, exception: IOException) {
        Toast.makeText(consumptionCreationFragment.context!!, message, Toast.LENGTH_LONG).show()
        Log.e("ConsumptionCreation", "dispatchTakePictureIntent: $message (Exception: ${exception.message}")
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File =
            consumptionCreationFragment.context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply { currentPhotoPath = absolutePath }
    }

    fun handleTakePictureResult() {
        Glide.with(consumptionCreationFragment.context!!)
            .load(currentPhotoPath)
            .into(consumptionCreationFragment.consumption_creation_image_view)
        consumptionCreationFragment.consumption_creation_image_view.apply {
            scaleType = ImageView.ScaleType.CENTER
            imageTintList = null
        }
    }

    companion object {
        private const val FILE_PROVIDER = "com.hotmail.leon.zimmermann.homeassistant.fileprovider"
    }
}