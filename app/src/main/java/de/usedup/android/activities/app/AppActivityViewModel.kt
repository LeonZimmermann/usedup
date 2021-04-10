package de.usedup.android.activities.app

import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.HiltViewModel
import de.usedup.android.R
import de.usedup.android.navigation.NavigationStateHolder
import javax.inject.Inject

@HiltViewModel
class AppActivityViewModel @Inject constructor(private val navigationStateHolder: NavigationStateHolder) : ViewModel() {

  fun restoreNavigationState(navController: NavController) {
    navigationStateHolder.navControllerState?.let {
      navController.restoreState(it)
      if (navigationStateHolder.shoppingList != null && navigationStateHolder.shoppingCart != null) {
        navController.navigate(R.id.action_global_shopping_fragment)
        navController.navigate(R.id.action_shopping_list_preview_fragment_to_shopping_list_fragment, bundleOf(
          "shoppingList" to navigationStateHolder.shoppingList,
          "shoppingCart" to navigationStateHolder.shoppingCart,
        ))
        navigationStateHolder.shoppingList = null
        navigationStateHolder.shoppingCart = null
      }
      navigationStateHolder.navControllerState = null
    }
  }

}