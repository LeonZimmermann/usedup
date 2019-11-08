package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestListener
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ConsumptionCreationPictureHandler {
    private var pictureBuffer: String? = null
    val hasPicture: Boolean
        get() = pictureBuffer != null

    fun dispatchTakePictureIntent(fragment: Fragment, requestCode: Int) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(fragment.context!!.packageManager)?.also {
            try {
                val file = createPictureBuffer(fragment)
                val pictureUri: Uri = FileProvider.getUriForFile(fragment.context!!, FILE_PROVIDER, file)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri)
                fragment.startActivityForResult(intent, requestCode)
            } catch (exception: IOException) {
                showToastAndLogErrorMessage(fragment, "Could not create image file", exception)
            }
        }
    }

    private fun showToastAndLogErrorMessage(fragment: Fragment, message: String, exception: IOException) {
        Toast.makeText(fragment.context!!, message, Toast.LENGTH_LONG).show()
        Log.e("ConsumptionCreation", "dispatchTakePictureIntent: $message (Exception: ${exception.message}")
    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createPictureBuffer(fragment: Fragment): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = fragment.context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply { pictureBuffer = absolutePath }
    }

    fun handleTakePictureResult(fragment: Fragment, target: ImageView) {
        Glide.with(fragment.context!!)
            .load(pictureBuffer)
            .into(target)
    }

    fun deletePictureBuffer(fragment: Fragment) {
        /*
        if (pictureBuffer != null) {
            fragment.context!!.deleteFile(pictureBuffer)
            pictureBuffer = null
        }
         */
    }

    companion object {
        private const val FILE_PROVIDER = "com.hotmail.leon.zimmermann.homeassistant.fileprovider"
    }
}