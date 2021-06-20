package de.usedup.android.invitationlist

import de.usedup.android.datamodel.api.objects.Id

data class InvitationRepresentation(
  val id: Id,
  val photoUrl: String?,
  val name: String,
  val message: String?,
)