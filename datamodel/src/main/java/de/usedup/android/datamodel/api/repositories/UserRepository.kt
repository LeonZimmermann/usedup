package de.usedup.android.datamodel.api.repositories

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.User
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single
import java.io.IOException

interface UserRepository {

  @Throws(IOException::class)
  suspend fun init()

  fun isInitialized(): Boolean

  @Throws(IOException::class)
  suspend fun getCurrentUser(): User

  fun getUser(id: Id): Single<User>

  fun getUsersFlowable(ids: Set<Id>): Flowable<User>

  @Throws(IOException::class)
  suspend fun addUser(id: Id, name: String, email: String, photoUrl: String?)

  @Throws(IOException::class)
  suspend fun updateUser(id: Id, name: String, email: String, photoUrl: String?)

  @Throws(IOException::class)
  suspend fun deleteUser(id: Id)

}