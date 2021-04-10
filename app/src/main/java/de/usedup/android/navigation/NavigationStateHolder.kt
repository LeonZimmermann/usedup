package de.usedup.android.navigation

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import de.usedup.android.shopping.data.ShoppingCart
import de.usedup.android.shopping.data.ShoppingList
import de.usedup.android.shopping.data.ShoppingProduct

object NavigationStateHolder {
  var navControllerState: Bundle? = null

  /*
   * Hacky solution, since saving the navigation state won't save the current navigation destination. Remove when
   * possible!
   */
  var shoppingList: ShoppingList? = null
  var shoppingCart: ShoppingCart? = null
}