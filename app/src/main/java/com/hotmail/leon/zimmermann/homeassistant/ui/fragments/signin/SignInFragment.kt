package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.signin

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.consumption_fragment_component.*
import kotlinx.android.synthetic.main.sign_in_fragment.*

class SignInFragment : Fragment() {

    companion object {
        fun newInstance() = SignInFragment()
    }

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        activity?.let { it.title = resources.getString(R.string.sign_in) }
        //email_button.setOnClickListener { findNavController().navigate(R.id.action_sign_in_fragment_to_email_signin_fragment) }
        //register_button.setOnClickListener { findNavController().navigate(R.id.action_sign_in_fragment_to_register_fragment) }
    }

}
