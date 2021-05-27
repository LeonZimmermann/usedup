package de.usedup.android.household

import de.usedup.android.R

data class HouseholdMemberRepresentation(
  val photoUrl: String,
  val name: String,
  val role: Role,
) {
  enum class Role(val ressourceId: Int) {
    ADMIN(R.string.admin),
    MEMBER(R.string.member),
  }
}
