package de.usedup.android.household

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import de.usedup.android.R
import de.usedup.android.datamodel.api.repositories.ImageRepository
import kotlinx.android.synthetic.main.household_member_item.view.*
import kotlinx.android.synthetic.main.meal_editor_fragment.*
import java.util.*

class HouseholdMemberRecyclerAdapter(private val context: Context, var callback: Callback) :
  RecyclerView.Adapter<HouseholdMemberRecyclerAdapter.HouseholdMemberViewHolder>() {

  private val entryPoint =
    EntryPointAccessors.fromApplication(context, HouseholdMemberRecyclerAdapterEntryPoint::class.java)
  private val imageRepository = entryPoint.getImageRepository()

  private var inflater = LayoutInflater.from(context)
  private var householdMembers: MutableList<HouseholdMemberRepresentation> = mutableListOf()

  inner class HouseholdMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userNameTextView: TextView = itemView.user_name_tv
    val userRoleTextView: TextView = itemView.user_role_tv
    val userImage: ImageView = itemView.user_image
    val previewButton: ImageView = itemView.preview_button
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HouseholdMemberViewHolder {
    return HouseholdMemberViewHolder(inflater.inflate(R.layout.household_member_item, parent, false))
  }

  override fun onBindViewHolder(holder: HouseholdMemberViewHolder, position: Int) {
    householdMembers[position].apply {
      holder.userNameTextView.text = name
      holder.userRoleTextView.text = context.getString(role.ressourceId)
      photoUrl?.let {
        imageRepository.getImage(it)
          .onErrorComplete()
          .subscribe { image ->
            Glide.with(context)
              .load(image)
              .into(holder.userImage)
          }
      }
      holder.previewButton.setOnClickListener {
        callback.onPreviewButtonClicked(this@HouseholdMemberRecyclerAdapter, position, holder.itemView, this)
      }
    }
  }

  override fun getItemCount(): Int = householdMembers.size

  internal fun initHouseholdMembers(householdMembers: Collection<HouseholdMemberRepresentation>) {
    this.householdMembers = householdMembers.toMutableList()
    notifyDataSetChanged()
  }

  internal fun addHouseholdMember(householdMemberRepresentation: HouseholdMemberRepresentation) {
    this.householdMembers.add(householdMemberRepresentation)
    notifyItemInserted(this.householdMembers.size - 1)
  }

  interface Callback {
    fun onPreviewButtonClicked(adapter: HouseholdMemberRecyclerAdapter, index: Int, view: View,
      householdMemberRepresentation: HouseholdMemberRepresentation)
  }

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface HouseholdMemberRecyclerAdapterEntryPoint {
    fun getImageRepository(): ImageRepository
  }
}