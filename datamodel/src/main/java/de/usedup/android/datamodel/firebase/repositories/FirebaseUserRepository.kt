package de.usedup.android.datamodel.firebase.repositories

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.User
import de.usedup.android.datamodel.api.repositories.UserRepository
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import de.usedup.android.datamodel.firebase.objects.FirebaseUser
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
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

  override fun isInitialized(): Boolean = currentUser != null

  private suspend fun createNewUser() = withContext(Dispatchers.IO) {
    val authUser = requireNotNull(FirebaseAuth.getInstance().currentUser)
    addUser(FirebaseId(authUser.uid), requireNotNull(authUser.displayName),
      requireNotNull(authUser.email), authUser.photoUrl?.toString())
    FirebaseHouseholdRepository.createHousehold(getCurrentUser().id)
  }

  override suspend fun getCurrentUser(): User = withContext(Dispatchers.IO) {
    if (currentUser == null) {
      val task = getDocumentReferenceToCurrentUser().get().apply { Tasks.await(this) }
      if (task.exception != null) {
        throw IOException(task.exception)
      } else {
        val result = task.result?.toObject<FirebaseUser>() ?: throw NoSuchElementException()
        currentUser = User.createInstance(result)
        requireNotNull(currentUser)
      }
    } else {
      requireNotNull(currentUser)
    }
  }

  override fun getUsersFlowable(ids: Set<Id>): Flowable<User> {
    return Flowable.create({ emitter ->
      ids.forEach { id ->
        val document = Tasks.await(collection.document((id as FirebaseId).value).get())
        val result = document.toObject<FirebaseUser>() ?: throw NoSuchElementException()
        if (emitter.isCancelled) return@forEach
        emitter.onNext(User.createInstance(result))
      }
      emitter.onComplete()
    }, BackpressureStrategy.BUFFER)
  }

  fun getDocumentReferenceToCurrentUser(): DocumentReference =
    collection.document(requireNotNull(FirebaseAuth.getInstance().uid))

  override suspend fun addUser(id: Id, name: String, email: String, photoUrl: String?) = withContext(Dispatchers.IO) {
    val firebaseUser = FirebaseUser((id as FirebaseId).value, name, email)
    val task = collection.document(id.value).set(firebaseUser).apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else currentUser = User.createInstance(firebaseUser)
  }

  override suspend fun updateUser(id: Id, name: String, email: String, photoUrl: String?) {
    withContext(Dispatchers.IO) {
      val data = mapOf(
        "name" to name,
        "email" to email
      )
      val task = collection.document((id as FirebaseId).value).update(data).apply { Tasks.await(this) }
      if (task.exception != null) throw IOException(task.exception)
      else {
        if (currentUser == null) currentUser = User(id, name, email, photoUrl)
        else currentUser?.let {
          it.name = name
          it.email = email
        }
      }
    }
  }

  override suspend fun deleteUser(id: Id) = withContext(Dispatchers.IO) {
    val task = collection.document((id as FirebaseId).value).delete().apply { Tasks.await(this) }
    if (task.exception != null) throw IOException(task.exception)
    else currentUser = null
  }

}