package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.User
import java.io.IOException

interface UserRepository {

  @Throws(IOException::class)
  suspend fun init()

  @Throws(IOException::class)
  suspend fun getCurrentUser(): User

  @Throws(IOException::class)
  suspend fun addUser(id: Id, name: String, email: String)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun updateUser(id: Id, name: String, email: String)

  @Throws(IOException::class, NoSuchElementException::class)
  suspend fun deleteUser(id: Id)

}