package de.usedup.android.datamodel.firebase.repositories

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Invitation
import de.usedup.android.datamodel.api.repositories.InvitationRepository
import de.usedup.android.datamodel.firebase.objects.FirebaseId
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

object FirebaseInvitationRepository : InvitationRepository {

  override suspend fun createInvitation(senderId: Id, receiverId: Id, message: String?) {
    // TODO senderId != receiverId and further validation
    TODO("not implemented")
  }

  override suspend fun getInvitationsForCurrentUserFlowable(): Flowable<Invitation> {
    return Flowable.just(
      Invitation(FirebaseId(""), FirebaseId("gw6LCSDRdKfOJLqbaLPpePt0bKw2"), FirebaseId("ZcWk5vlLxre2gD4S2QoxMlQx9h92"),
        "Test"))
  }

  override suspend fun getInvitation(id: Id): Single<Invitation> {
    return Single.just(
      Invitation(FirebaseId(""), FirebaseId("gw6LCSDRdKfOJLqbaLPpePt0bKw2"), FirebaseId("ZcWk5vlLxre2gD4S2QoxMlQx9h92"),
        "Test"))
  }

  override suspend fun deleteInvitation(id: Id) {
    TODO("not implemented")
  }
}