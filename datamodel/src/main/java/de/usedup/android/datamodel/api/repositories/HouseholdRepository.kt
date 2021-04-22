package de.usedup.android.datamodel.api.repositories

import androidx.lifecycle.LiveData
import de.usedup.android.datamodel.api.objects.Household
import de.usedup.android.datamodel.api.objects.Id
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineScope

interface HouseholdRepository {
  fun createHousehold(adminId: Id)
  fun getHouseholdLiveData(coroutineScope: CoroutineScope): LiveData<Household>
  fun getHousehold(): Single<Household>
  suspend fun addMember(memberId: Id)
  suspend fun removeMember(memberId: Id)
}