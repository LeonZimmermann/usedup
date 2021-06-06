package de.usedup.android.household

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import de.usedup.android.R
import kotlinx.android.synthetic.main.email_input_dialog.view.*

class EmailInputDialog : DialogFragment() {

  var callback: Callback? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return activity?.let {
      val view = requireActivity().layoutInflater.inflate(R.layout.email_input_dialog, null)
      MaterialAlertDialogBuilder(requireContext())
        .setView(view)
        .setTitle(R.string.invite_user)
        .setPositiveButton(R.string.submit) { _, _ ->
          callback?.onPositiveButtonClicked(view.email_input.text.toString())
        }
        .setNegativeButton(R.string.cancel) { dialogInterface, _ -> dialogInterface.cancel() }
        .create()
    } ?: throw IllegalArgumentException("Activity cannot be null")
  }

  interface Callback {
    fun onPositiveButtonClicked(email: String)
  }

  companion object {
    fun newInstance() = EmailInputDialog()
  }
}