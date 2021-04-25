package de.usedup.android.preferences

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.usedup.android.R

class PreferenceFragment: Fragment() {

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.preference_fragment, container, false)
  }

  companion object {
    fun newInstance() = PreferenceFragment()
  }
}