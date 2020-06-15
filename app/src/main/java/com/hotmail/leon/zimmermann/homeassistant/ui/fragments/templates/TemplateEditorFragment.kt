package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.templates

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hotmail.leon.zimmermann.homeassistant.R

class TemplateEditorFragment : Fragment() {

    companion object {
        fun newInstance() = TemplateEditorFragment()
    }

    private lateinit var viewModel: TemplateEditorViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.template_editor_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TemplateEditorViewModel::class.java)
        // TODO: Use the ViewModel
    }

}