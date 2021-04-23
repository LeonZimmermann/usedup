package de.usedup.android.datamodel.api.repositories

import android.graphics.Bitmap
import android.net.Uri
import io.reactivex.rxjava3.core.Maybe

interface ImageRepository {
  fun getImage(name: String): Maybe<Bitmap>
  suspend fun createImage(name: String, bitmap: Bitmap): Uri
  suspend fun deleteImage(name: String)
}