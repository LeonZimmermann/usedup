package de.usedup.android.datamodel.firebase.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import com.google.android.gms.tasks.Tasks
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import de.usedup.android.datamodel.api.repositories.ImageRepository
import io.reactivex.rxjava3.core.Maybe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.IOException


object FirebaseImageRepository : ImageRepository {

  private const val IMAGES_PATH = "images/"
  private const val MAX_IMAGE_DOWNLOAD_SIZE: Long = 1024 * 1024 * 1024

  private val cache: MutableMap<String, Bitmap> = mutableMapOf()

  override fun getImage(name: String): Maybe<Bitmap> {
    return Maybe.fromCallable {
      if (cache.containsKey(name)) cache[name]
      else {
        val reference = Firebase.storage.reference.child(IMAGES_PATH + name)
        val task = reference.getBytes(MAX_IMAGE_DOWNLOAD_SIZE).apply { Tasks.await(this) }
        if (task.isSuccessful) {
          val data: ByteArray = requireNotNull(task.result)
          val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
          cache[name] = bitmap
          bitmap
        } else {
          null
        }
      }
    }
  }

  override suspend fun createImage(name: String, bitmap: Bitmap): Uri = withContext(Dispatchers.IO) {
    val reference = Firebase.storage.reference.child(IMAGES_PATH + name)
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
    val data = outputStream.toByteArray()
    val task = reference.putBytes(data).continueWithTask {
      if (!it.isSuccessful) it.exception?.let { exception -> throw exception }
      reference.downloadUrl
    }.apply { Tasks.await(this) }
    if (task.isSuccessful) {
      cache[name] = bitmap
      requireNotNull(task.result)
    } else {
      throw IOException(task.exception)
    }
  }

  override suspend fun deleteImage(name: String) {
    withContext(Dispatchers.IO) {
      val task = Firebase.storage.reference.child(IMAGES_PATH + name).delete().apply { Tasks.await(this) }
      if (task.exception != null) {
        throw IOException(task.exception)
      } else {
        cache.remove(name)
      }
    }
  }
}