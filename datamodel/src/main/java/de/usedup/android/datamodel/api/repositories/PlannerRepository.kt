package de.usedup.android.datamodel.api.repositories

import androidx.lifecycle.MutableLiveData
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.PlannerItem

interface PlannerRepository {

  /**
   * Holds a livedata of all items that are available in the database and belong to this user.
   */
  val plan: MutableLiveData<MutableList<PlannerItem>>

  /**
   * Initializes the plan field of the repository. Should be run on IO Dispatcher, as it fetches data from the database.
   * Should also update the plan, so that deprecated PlannerItems are removed from the database.
   */
  suspend fun init()

  /**
   * Loads the PlannerItems blocking in the current thread if it hasn't been loaded yet. Otherwise the value in the plan
   * field should be returned.
   *
   * @return a list of planner items
   */
  fun getAllPlannerItems(): List<PlannerItem>

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