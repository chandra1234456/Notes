package com.chandra.practice.notesmvvm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserSearchAdapter(private var userList: List<User>) : RecyclerView.Adapter<UserSearchAdapter.UserViewHolder>() {
    // ViewHolder class to hold the view for each item
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val noteDescription: TextView = itemView.findViewById(R.id.noteDescription)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_user_search_item, parent, false) // Layout for each item
        return UserViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.noteTitle.text = user.noteTitle
        holder.noteDescription.text = user.noteDescription
    }

    // Return the size of the dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return userList.size
    }

    // Update the user list
    fun updateUserList(newUserList: List<User>) {
        userList = newUserList
        notifyDataSetChanged()  // Notify the adapter that the data has changed
    }
}
