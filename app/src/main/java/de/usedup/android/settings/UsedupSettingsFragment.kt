package de.usedup.android.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import de.usedup.android.databinding.UsedupSettingsFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R

@AndroidEntryPoint
class UsedupSettingsFragment : Fragment() {

  private val viewModel: UsedupSettingsViewModel by viewModels()

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.usedup_settings_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
    initErrorMessage()
  }

  private fun initDatabinding() {
    val binding = UsedupSettingsFragmentBinding.bind(requireView())
    binding.lifecycleOwner = this
    binding.viewModel = viewModel
  }

  private fun initErrorMessage() {
    viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
      Snackbar.make(requireView(), errorMessage, Snackbar.LENGTH_LONG).show()
    })
  }

  companion object {
    fun newInstance() = UsedupSettingsFragment()
  }
}