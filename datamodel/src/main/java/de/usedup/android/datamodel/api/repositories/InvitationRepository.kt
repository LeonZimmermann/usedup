package de.usedup.android.datamodel.api.repositories

import de.usedup.android.datamodel.api.objects.Id
import de.usedup.android.datamodel.api.objects.Invitation
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Single

interface InvitationRepository {

  suspend fun createInvitation(senderId: Id, receiverId: Id, message: String?)

  suspend fun getInvitationsForCurrentUserFlowable(): Flowable<Invitation>

  suspend fun getInvitation(id: Id): Single<Invitation>

  suspend fun deleteInvitation(id: Id)
}