package com.hotmail.leon.zimmermann.homeassistant.app.ui.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.camera_fragment.*
import org.jetbrains.anko.support.v4.toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraFragment : Fragment() {

    private lateinit var photoUri: Uri
    private var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initFile(savedInstanceState)
        initPhotoUri(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.hide()
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar!!.show()
    }

    private fun initFile(savedInstanceState: Bundle?) {
        file = arguments?.getSerializable(FILE) as? File
            ?: savedInstanceState?.getSerializable(FILE) as? File
                    ?: throw RuntimeException("No File parameter supplied!")
    }

    private fun initPhotoUri(savedInstanceState: Bundle?) {
        var photoUri: Uri?
        photoUri = savedInstanceState?.getParcelable(PHOTO_URI)
        if (photoUri == null) {
            try {
                photoUri = dispatchTakePictureIntent()
            } catch (e: IOException) {
                if (e.message != null) toast(e.message!!)
            }
        }
        this.photoUri = photoUri!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.camera_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        discard_button.setOnClickListener { findNavController().navigateUp() }
        accept_button.setOnClickListener {
            cropImageView.getCroppedImage().compress(Bitmap.CompressFormat.JPEG, 100, file!!.outputStream())
            findNavController().navigateUp()
        }
    }

    private fun initCropImageView() {
        file?.let {
            cropImageView.setImage(BitmapFactory.decodeStream(it.inputStream()))
        }
    }

    @Throws(IOException::class)
    private fun dispatchTakePictureIntent(): Uri {
        if (!context!!.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
            throw NoCameraException("Device has no camera")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val photoUri =
            FileProvider.getUriForFile(context!!, "com.hotmail.leon.zimmermann.homeassistant.fileprovider", file!!)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
        intent.resolveActivity(context!!.packageManager)?.also {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
        return photoUri
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE) initCropImageView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (photoUri != null) outState.putParcelable(PHOTO_URI, photoUri)
        if (file != null) outState.putSerializable(FILE, file)
    }

    companion object {
        private const val PHOTO_URI = "photo_uri"
        private const val FILE = "file"
        private const val REQUEST_IMAGE_CAPTURE = 1

        @SuppressLint("SimpleDateFormat")
        private val timestampFormatter = SimpleDateFormat("yyyyMMdd_HHmmss")

        @Throws(IOException::class)
        fun createPhotoFile(context: Context): File {
            val timeStamp: String = timestampFormatter.format(Date())
            val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                ?: throw IOException("Could not access external files dir")
            val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
            file.deleteOnExit()
            return file
        }

        fun newInstance() = CameraFragment()
    }

    class NoCameraException(message: String) : Exception(message)
}
