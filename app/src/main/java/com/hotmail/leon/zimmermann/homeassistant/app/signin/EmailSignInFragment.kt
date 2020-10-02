package com.hotmail.leon.zimmermann.homeassistant.app.signin

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hotmail.leon.zimmermann.homeassistant.R

class EmailSignInFragment : Fragment() {

    companion object {
        fun newInstance() = EmailSignInFragment()
    }

    private lateinit var viewModel: EmailSignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.email_sign_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(EmailSignInViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
