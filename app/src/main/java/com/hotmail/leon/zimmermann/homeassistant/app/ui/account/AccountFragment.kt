package com.hotmail.leon.zimmermann.homeassistant.app.ui.account

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth

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
            user.photoUrl?.let {
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(profile_image)
            }
            user.displayName?.let { name_input.setText(it) }
            user.email?.let { email_input.setText(it) }
        } ?: run {
            Toast.makeText(context, "Cannot access account information", Toast.LENGTH_LONG).show()
            findNavController().navigateUp()
        }
        logout_button.setOnClickListener {
            authentication.signOut()
            // TODO Navigate out of app
        }
    }

}
