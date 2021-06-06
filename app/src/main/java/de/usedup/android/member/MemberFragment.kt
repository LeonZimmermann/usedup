package de.usedup.android.member

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import de.usedup.android.R
import de.usedup.android.databinding.MemberFragmentBinding
import de.usedup.android.datamodel.api.objects.Id

@AndroidEntryPoint
class MemberFragment : Fragment() {

  private val viewModel: MemberViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.apply {
      viewModel.setMember(getSerializable(MEMBER_ID) as Id)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.member_fragment, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initDatabinding()
  }

  private fun initDatabinding() {
    val binding = MemberFragmentBinding.bind(requireView())
    binding.viewModel = viewModel
    binding.lifecycleOwner = this
  }

  companion object {
    const val MEMBER_ID = "member_id"

    fun newInstance() = MemberFragment()
  }
}