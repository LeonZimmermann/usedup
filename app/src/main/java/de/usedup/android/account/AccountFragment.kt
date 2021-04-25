package de.usedup.android.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.activities.main.MainActivity
import de.usedup.android.databinding.AccountFragmentBinding

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
    initStartActivityCallback()
    initNavigateUpCallback()
  }

  private fun initDatabinding() {
    val binding = AccountFragmentBinding.bind(requireView())
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initErrorMessage() {
    viewModel.errorMessage.observe(viewLifecycleOwner, { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }

  private fun initStartActivityCallback() {
    viewModel.backToMainActivity.observe(viewLifecycleOwner) { backToMainActivity ->
      if (backToMainActivity) {
        val intent = Intent(context, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_CLEAR_TOP }
        requireContext().startActivity(intent)
        viewModel.backToMainActivity.postValue(false)
      }
    }
  }

  private fun initNavigateUpCallback() {
    viewModel.navigateUp.observe(viewLifecycleOwner) { navigateUp ->
      if (navigateUp) {
        Navigation.findNavController(requireView()).navigateUp()
        viewModel.navigateUp.postValue(false)
      }
    }
  }

  companion object {
    fun newInstance() = AccountFragment()
  }

}