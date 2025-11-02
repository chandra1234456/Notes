package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.User
import com.chandra.practice.notesmvvm.UserSearchAdapter
import com.chandra.practice.notesmvvm.databinding.FragmentSearchBinding
import java.util.Locale

class SearchFragment : Fragment() {
    private lateinit var searchBinding : FragmentSearchBinding
    private lateinit var userList: List<User>
    private  var filteredUserList: MutableList<User> = mutableListOf()
    private lateinit var adapter: UserSearchAdapter  // Assume you have an adapter to display users

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        searchBinding = FragmentSearchBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(searchBinding.root) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemInsets.left,
                systemInsets.top,
                systemInsets.right,
                systemInsets.bottom
            )
            insets
        }


        searchBinding.cancel.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        searchBinding.ivNoRecord.visibility =View.VISIBLE
        searchBinding.recyclerView.visibility =View.GONE

        adapter = UserSearchAdapter(filteredUserList) // Initialize your adapter with the filtered list
        searchBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        searchBinding.recyclerView.adapter = adapter

        searchBinding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No need to implement if you don't want anything here
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {
                    filterList(searchText)
                    searchBinding.recyclerView.visibility =View.VISIBLE
                    searchBinding.ivNoRecord.visibility = View.GONE
                }else{
                    searchBinding.recyclerView.visibility = View.GONE
                    searchBinding.ivNoRecord.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No need to implement if you don't want anything here
            }
        })
        searchBinding.searchInput.setOnTouchListener { v, event ->
            val DRAWABLE_END = 2  // Index for end drawable (right side)

            if (event.action == MotionEvent.ACTION_UP) {
                val editText = v as EditText
                // Check if the touch is within the bounds of the end drawable
                if (event.rawX >= (editText.right - editText.compoundDrawables[DRAWABLE_END].bounds.width())) {
                    // Clear the text when end icon is clicked
                    editText.text.clear()
                    return@setOnTouchListener true
                }
            }
            false
        }

        return searchBinding.root
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userList = it.getParcelableArrayList("userList") ?: emptyList()
        }
    }
    private fun filterList(query: String) {
        filteredUserList.clear()
        if (query.isEmpty()) {
            filteredUserList.addAll(userList)
        } else {
            for (user in userList) {
                if (user.noteTitle.lowercase(Locale.getDefault()).contains(query) ||
                    user.noteDescription.lowercase(Locale.getDefault()).contains(query)) {
                    filteredUserList.add(user)
                }else{
                    searchBinding.recyclerView.visibility =View.GONE
                    searchBinding.ivNoRecord.visibility =View.VISIBLE
                }
            }
        }
        adapter.notifyDataSetChanged()  // Notify the adapter about the changes
    }

}