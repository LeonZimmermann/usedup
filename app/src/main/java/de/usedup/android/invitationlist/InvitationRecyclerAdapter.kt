package de.usedup.android.invitationlist

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
import kotlinx.android.synthetic.main.invitation_list_element.view.*

class InvitationRecyclerAdapter(private val context: Context, var callback: Callback) :
  RecyclerView.Adapter<InvitationRecyclerAdapter.InvitationViewHolder>() {

  private val entryPoint =
    EntryPointAccessors.fromApplication(context, InvitationRecyclerAdapterEntryPoint::class.java)
  private val imageRepository = entryPoint.getImageRepository()

  private var inflater = LayoutInflater.from(context)
  private var invitations: MutableList<InvitationRepresentation> = mutableListOf()

  inner class InvitationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userNameTextView: TextView = itemView.user_name_tv
    val userImage: ImageView = itemView.user_image
    val previewButton: ImageView = itemView.preview_button
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InvitationViewHolder {
    return InvitationViewHolder(inflater.inflate(R.layout.invitation_list_element, parent, false))
  }

  override fun onBindViewHolder(holder: InvitationViewHolder, position: Int) {
    invitations[position].apply {
      holder.userNameTextView.text = name
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
        callback.onPreviewButtonClicked(this@InvitationRecyclerAdapter, position, holder.itemView, this)
      }
    }
  }

  override fun getItemCount(): Int = invitations.size

  internal fun initInvitations(invitations: Collection<InvitationRepresentation>) {
    this.invitations = invitations.toMutableList()
    notifyDataSetChanged()
  }

  internal fun addInvitation(invitationRepresentation: InvitationRepresentation) {
    this.invitations.add(invitationRepresentation)
    notifyItemInserted(this.invitations.size - 1)
  }

  interface Callback {
    fun onPreviewButtonClicked(adapter: InvitationRecyclerAdapter, index: Int, view: View,
      invitationRepresentation: InvitationRepresentation)
  }

  @EntryPoint
  @InstallIn(SingletonComponent::class)
  interface InvitationRecyclerAdapterEntryPoint {
    fun getImageRepository(): ImageRepository
  }
}