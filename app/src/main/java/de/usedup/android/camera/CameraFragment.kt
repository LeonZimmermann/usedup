package de.usedup.android.camera

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.databinding.CameraFragmentBinding
import kotlinx.android.synthetic.main.camera_fragment.*
import org.jetbrains.anko.support.v4.toast

@AndroidEntryPoint
class CameraFragment : Fragment() {

  private val viewModel: CameraViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.dispatchTakePictureIntent()
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.camera_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initCropImageView()
    initCameraIntentHandler()
    initErrorMessageHandler()
    initNavigateUpHandler()
    viewModel.dispatchTakePictureIntent()
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == REQUEST_IMAGE_CAPTURE) {
      cropImageView.setImageBitmap(BitmapFactory.decodeStream(viewModel.file.inputStream()))
    }
  }

  private fun initDatabinding() {
    val binding = CameraFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  override fun onResume() {
    super.onResume()
    requireNotNull((activity as AppCompatActivity).supportActionBar).hide()
  }

  override fun onPause() {
    super.onPause()
    requireNotNull((activity as AppCompatActivity).supportActionBar).show()
  }

  private fun initCropImageView() {
    cropImageView.setImageBitmap(BitmapFactory.decodeStream(viewModel.file.inputStream()))
  }

  private fun initCameraIntentHandler() {
    viewModel.cameraIntent.observe(viewLifecycleOwner) {
      it?.let {
        startActivityForResult(it, REQUEST_IMAGE_CAPTURE)
        viewModel.cameraIntent.postValue(null)
      }
    }
  }

  private fun initErrorMessageHandler() {
    viewModel.errorMessage.observe(viewLifecycleOwner) {
      it?.let {
        toast(it)
        viewModel.errorMessage.postValue(null)
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

  companion object {
    private const val REQUEST_IMAGE_CAPTURE = 1
    const val BITMAP = "bitmap"

    fun newInstance() = CameraFragment()
  }
}
