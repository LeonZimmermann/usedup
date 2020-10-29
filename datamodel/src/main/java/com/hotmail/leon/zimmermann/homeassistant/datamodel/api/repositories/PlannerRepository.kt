package com.hotmail.leon.zimmermann.homeassistant.datamodel.api.repositories

import androidx.lifecycle.MutableLiveData
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.Id
import com.hotmail.leon.zimmermann.homeassistant.datamodel.api.objects.PlannerItem
import java.time.LocalDate

interface PlannerRepository {

  /**
   * Holds a livedata of all items that are available in the database and belong to this user
   */
  val plan: MutableLiveData<MutableList<PlannerItem>>

  /**
   * Initializes the plan field of the repository. Should be run on IO Dispatcher, as it fetches data from the database.
   */
  suspend fun init()

  /**
   * Adds a new item to the repository and database. Should be run on IO Dispatcher, as it fetches data from the database.
   *
   * @param id the id of the PlannerItem to be updated
   * @param mealId the mealId of the PlannerItem
   * @param date the date of the PlannerItem as a long
   * @throws IOException when the connection to the database fails
   */
  suspend fun addPlannerItem(mealId: Id, date: Long)

  /**
   * Updates an existing item of the repository and database. Should be run on IO Dispatcher, as it fetches data from the database.
   *
   * @param id the id of the PlannerItem to be updated
   * @param mealId the mealId of the PlannerItem
   * @param date the date of the PlannerItem as a long
   * @throws IOException when the connection to the database fails
   */
  suspend fun updatePlannerItem(id: Id, mealId: Id, date: Long)

  /**
   * Deletes an existing item of the repository and database. Should be run on IO Dispatcher, as it fetches data from the database.
   *
   * @param id the id of the PlannerItem to be deleted
   * @throws IOException when the connection to the database fails
   */
  suspend fun deletePlannerItem(id: Id)
}