package com.google.chatappstream.presentation.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.chatappstream.databinding.FragmentUserListRcvBinding
import io.getstream.chat.android.client.models.User

class UserListAdapter(val onUserClick: (selectedUser: User) -> Unit) :
    ListAdapter<User, UserListAdapter.MyViewHolder>(diffUtil) {

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem

        }
    }

    inner class MyViewHolder(private val binding: FragmentUserListRcvBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.rootLayout.setOnClickListener {
                onUserClick(getItem(bindingAdapterPosition))
            }
        }

        fun bind(item: User) {
            binding.apply {
                avatarImageView.setUserData(item)
                usernameTextView.text = item.id
                lastActiveTextView.text = "${item.lastActive?.time}"

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FragmentUserListRcvBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(getItem(position))
    }


}

