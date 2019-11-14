package com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.edit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.arch.core.util.Function
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.leon.zimmermann.homeassistant.R
import com.hotmail.leon.zimmermann.homeassistant.ui.components.SimpleProductPreviewAdapter
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.ShoppingEntry
import com.hotmail.leon.zimmermann.homeassistant.ui.fragments.shopping.ShoppingViewModel
import kotlinx.android.synthetic.main.shopping_edit_fragment.*
import java.io.Console

class ShoppingEditFragment : Fragment() {

    private lateinit var viewModel: ShoppingViewModel
    private lateinit var adapter: ShoppingEditListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.shopping_edit_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = activity?.run {
            ViewModelProviders.of(this).get(ShoppingViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        adapter = ShoppingEditListAdapter(context!!, View.OnClickListener { onEntryClicked(it) })
        shopping_edit_list.layoutManager = LinearLayoutManager(context!!)
        shopping_edit_list.adapter = adapter
        viewModel.shoppingList.observe(this, Observer { shoppingList ->
            adapter.setShoppingList(shoppingList)
        })
        add_button.setOnClickListener { onAddButtonClicked() }
    }

    private fun onEntryClicked(view: View) {
        viewModel.editShoppingEntryIndex = shopping_edit_list.indexOfChild(view)
        ShoppingEditDialogFragment().show(fragmentManager!!, "ShoppingEditDialog")
    }

    private fun onAddButtonClicked() {
        ShoppingEditDialogFragment().show(fragmentManager!!, "ShoppingEditDialog")
    }
}