package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.register

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.consumption_fragment_component.*
import kotlinx.android.synthetic.main.register_fragment.*

class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.register_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        activity?.let { it.title = resources.getString(R.string.register) }
        //email_button.setOnClickListener { findNavController().navigate(R.id.action_register_fragment_to_email_registration_fragment) }
        //signin_button.setOnClickListener { findNavController().navigate(R.id.action_register_fragment_to_sign_in_fragment) }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}
