package de.usedup.android.camera

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.canhub.cropper.CropImageView
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.usedup.android.datamodel.api.repositories.ImageRepository
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
  @ApplicationContext private val context: Context,
) : ViewModel() {

  @SuppressLint("SimpleDateFormat")
  private val timestampFormatter = SimpleDateFormat("yyyyMMdd_HHmmss")

  var file: File = createPhotoFile()

  val cameraIntent: MutableLiveData<Intent?> = MutableLiveData()
  val errorMessage: MutableLiveData<String?> = MutableLiveData()
  val navigateUp: MutableLiveData<Boolean> = MutableLiveData()

  private fun createPhotoFile(): File {
    val timeStamp: String = timestampFormatter.format(Date())
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
      ?: throw IOException("Could not access external files dir")
    val file = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    file.deleteOnExit()
    return file
  }

  fun dispatchTakePictureIntent() {
    try {
      if (!context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY))
        throw IOException("Device has no camera")
      val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
      val photoUri = FileProvider.getUriForFile(context, "de.usedup.android.fileprovider", file)
      intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
      intent.resolveActivity(context.packageManager)?.also {
        cameraIntent.postValue(intent)
      }
    } catch (e: IOException) {
      errorMessage.postValue(e.message)
      navigateUp.postValue(true)
    }
  }

  fun onAcceptClicked(view: View, cropImageView: CropImageView) {
    cropImageView.croppedImage?.let {
      view.findNavController().previousBackStackEntry?.savedStateHandle?.set(CameraFragment.BITMAP, it)
      navigateUp.postValue(true)
    }
  }

  fun onDiscardClicked(view: View) {
    navigateUp.postValue(true)
  }
}