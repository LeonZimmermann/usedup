package de.usedup.android.datamodel.api.repositories

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.User
import java.io.IOException

interface UserRepository {

  @Throws(IOException::class)
  suspend fun init()

  @Throws(IOException::class)
  suspend fun getCurrentUser(): User

  @Throws(IOException::class)
  suspend fun addUser(id: Id, name: String, email: String)

  @Throws(IOException::class)
  suspend fun updateUser(id: Id, name: String, email: String)

  @Throws(IOException::class)
  suspend fun deleteUser(id: Id)

}