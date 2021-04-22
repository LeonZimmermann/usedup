package de.usedup.android.shopping.list

class ShoppingListCategoryRepresentation(val name: String, val position: Int,
  val shoppingListElementRepresentation: Set<ShoppingListElementRepresentation>) {
  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false
    other as ShoppingListCategoryRepresentation
    if (name != other.name) return false
    return true
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }
}