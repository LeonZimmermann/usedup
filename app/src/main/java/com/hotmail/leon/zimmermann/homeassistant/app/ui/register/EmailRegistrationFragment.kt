package com.hotmail.leon.zimmermann.homeassistant.app.ui.register

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hotmail.leon.zimmermann.homeassistant.R

class EmailRegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = EmailRegistrationFragment()
    }

    private lateinit var viewModel: EmailRegistrationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.email_registration_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EmailRegistrationViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
