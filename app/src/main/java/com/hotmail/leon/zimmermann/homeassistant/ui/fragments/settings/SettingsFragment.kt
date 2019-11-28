package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.settings

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.models.tables.category.Category
import com.hotmail.leon.zimmermann.homeassistant.ui.components.categoryOrder.CategoryOrderDialogFragment
import kotlinx.coroutines.launch
import org.jetbrains.anko.support.v4.toast

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var viewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        findPreference<Preference>("category_order")?.onPreferenceClickListener =
            Preference.OnPreferenceClickListener { preference: Preference ->
                CategoryOrderDialogFragment(
                    viewModel.categoryOrder,
                    object : CategoryOrderDialogFragment.OnChangedListener {
                        override fun onChanged(categoryOrder: List<Category>) {
                            viewModel.setCategoryOrder(categoryOrder)
                        }
                    },
                    object : CategoryOrderDialogFragment.OnResetListener {
                        override fun onReset() {
                            viewModel.setCategoryOrder(Category.values().toList())
                        }
                    }).show(fragmentManager!!, CATEGORY_ORDER_DIALOG)
                true
            }
    }

    companion object {
        private const val CATEGORY_ORDER_DIALOG = "CategoryOrderDialog"
    }

}