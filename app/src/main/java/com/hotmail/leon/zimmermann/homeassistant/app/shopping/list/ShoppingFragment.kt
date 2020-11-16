package com.hotmail.leon.zimmermann.homeassistant.app.shopping.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.shopping_fragment.*

@AndroidEntryPoint
class ShoppingFragment : Fragment() {

  private val viewModel: ShoppingViewModel by viewModels()

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    return inflater.inflate(R.layout.shopping_fragment, container, false)
  }

  companion object {
    fun newInstance() = ShoppingFragment()
  }
}
