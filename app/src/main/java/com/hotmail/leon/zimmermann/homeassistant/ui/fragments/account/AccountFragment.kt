package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.account

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

import com.hotmail.leon.zimmermann.homeassistant.R
import kotlinx.android.synthetic.main.account_fragment.*

class AccountFragment : Fragment() {

    companion object {
        fun newInstance() = AccountFragment()
    }

    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.account_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        val authentication = FirebaseAuth.getInstance()
        authentication.currentUser?.let { user ->
            name_input.setText(user.displayName)
            email_input.setText(user.email)
        } ?: run {
            Toast.makeText(context, "Cannot access account information", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
        logout_button.setOnClickListener {
            authentication.signOut()
            findNavController().navigate(R.id.splash_screen_fragment)
            findNavController().popBackStack()
        }
    }

}
