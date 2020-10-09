package com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.User
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories.UserRepository
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseId
import com.hotmail.leon.zimmermann.homeassistant.datamodel.firebase.objects.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object FirebaseUserRepository : UserRepository {
  private val collection = Firebase.firestore.collection(FirebaseUser.COLLECTION_NAME)

  private var currentUser: User? = null

  override suspend fun init() {
    withContext(Dispatchers.IO) {
      try {
        getCurrentUser()
      } catch (e: NoSuchElementException) {
        createNewUser()
      }
    }
  }

  private suspend fun createNewUser() = withContext(Dispatchers.IO) {
    val authUser = requireNotNull(FirebaseAuth.getInstance().currentUser)
    addUser(FirebaseId(authUser.uid), requireNotNull(authUser.displayName),
      requireNotNull(authUser.email))
  }

  override suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
    if (currentUser != null) currentUser!!
    else {
      val userId = requireNotNull(FirebaseAuth.getInstance().uid)
      val task = collection.whereEqualTo("id", userId).get().apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(task.exception)
      else {
        currentUser = User.createInstance(task.result!!.first().toObject())
        currentUser!!
      }
    }
  }

  override suspend fun addUser(id: Id, name: String, email: String) = withContext(Dispatchers.IO) {
    val firebaseUser = FirebaseUser((id as FirebaseId).value, name, email)
    val task = collection.document(id.value).set(firebaseUser).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else currentUser = User.createInstance(firebaseUser)
  }

  override suspend fun updateUser(id: Id, name: String, email: String) = withContext(Dispatchers.IO) {
    val data = mapOf(
      "name" to name,
      "email" to email
    )
    val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else {
      if (currentUser == null) currentUser = User(id, name, email)
      else currentUser!!.let {
        it.name = name
        it.email = email
      }
    }
  }

  override suspend fun deleteUser(id: Id) = withContext(Dispatchers.IO) {
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else currentUser = null
  }

}