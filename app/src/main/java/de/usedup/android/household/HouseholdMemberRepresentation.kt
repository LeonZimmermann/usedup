package de.usedup.android.household

import de.usedup.android.R
import de.usedup.android.datamodel.api.objects.Id

data class HouseholdMemberRepresentation(
  val id: Id,
  val photoUrl: String?,
  val name: String,
  val role: Role,
) {
  enum class Role(val ressourceId: Int) {
    ADMIN(R.string.admin),
    MEMBER(R.string.member),
  }
}
