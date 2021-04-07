package de.usedup.android.datamodel.api.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.PlannerItem
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope

interface PlannerRepository {

  /**
   * Lazily loads the PlannerItems in a IO-Coroutine. If the data was already the already available value is returned.
   *
   * @return a list of planner items
   */
  fun getAllPlannerItemsLiveData(coroutineScope: CoroutineScope): LiveData<List<PlannerItem>>

  /**
   * Returns a Single object that returns all planner items. The Single object can be subscribed on to asynchronously
   * retrieve its value.
   *
   * @return a list of planner items
   */
  fun getAllPlannerItems(): Single<List<PlannerItem>>

  /**
   * Adds a new item to the repository and database. Should be run on IO Dispatcher, as it fetches data from the database.
   *
   * @param mealId the mealId of the PlannerItem
   * @param date the date of the PlannerItem as a long
   */
  suspend fun addPlannerItem(mealId: Id, date: Long)

  /**
   * Updates an existing item of the repository and database. Should be run on IO Dispatcher, as it fetches data from the database.
   *
   * @param id the id of the PlannerItem to be updated
   * @param mealId the mealId of the PlannerItem
   * @param date the date of the PlannerItem as a long
   */
  suspend fun updatePlannerItem(id: Id, mealId: Id, date: Long)

  /**
   * Deletes an existing item of the repository and database. Should be run on IO Dispatcher, as it fetches data from the database.
   *
   * @param id the id of the PlannerItem to be deleted
   */
  suspend fun deletePlannerItem(id: Id)
}