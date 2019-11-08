package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.consumption.creation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.consumption_creation_picture_preview_fragment.*
import androidx.appcompat.app.AppCompatActivity
import com.hotmail.leon.zimmermann.homeassistant.R


class ConsumptionCreationPicturePreviewFragment : Fragment() {

    private val pictureHandler = ConsumptionCreationPictureHandler()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (!pictureHandler.hasPicture)
            pictureHandler.dispatchTakePictureIntent(this, REQUEST_IMAGE_CAPTURE)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.consumption_creation_picture_preview_fragment, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            pictureHandler.handleTakePictureResult(this, picture_view)
            pictureHandler.deletePictureBuffer(this)
        }
    }

    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}