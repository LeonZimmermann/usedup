package com.hotmail.leon.zimmermann.homeassistant.app.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.databinding.AccountFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

  private val viewModel: AccountViewModel by viewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.account_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initErrorMessage()
  }

  private fun initDatabinding() {
    val binding = AccountFragmentBinding.bind(requireView())
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initErrorMessage() {
    viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }

  companion object {
    fun newInstance() = AccountFragment()
  }
}
