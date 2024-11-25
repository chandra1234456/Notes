package com.chandra.practice.notesmvvm.fragments

import android.content.ClipData
import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chandra.practice.notesmvvm.AppDatabase
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.User
import com.chandra.practice.notesmvvm.UserAdapter
import com.chandra.practice.notesmvvm.UserRepository
import com.chandra.practice.notesmvvm.UserViewModel
import com.chandra.practice.notesmvvm.UserViewModelFactory
import com.chandra.practice.notesmvvm.databinding.FragmentHomeBinding
import com.chandra.practice.notesmvvm.util.showSnackBar

class HomeFragment : Fragment() , UserAdapter.OnItemClickLister {
    private lateinit var userViewModel : UserViewModel
    private lateinit var userAdapter : UserAdapter
    private lateinit var homeBinding : FragmentHomeBinding
    private  var userList: ArrayList<User> = ArrayList()
    private var isLinearLayout = true // To keep track of layout state

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        homeBinding = FragmentHomeBinding.inflate(layoutInflater)
        val dao = AppDatabase.getDatabase(requireContext()).userDao()
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this , factory)[UserViewModel::class.java]
        userAdapter = UserAdapter(this , requireContext()) // Initialize your RecyclerView adapter
        homeBinding.recyclerView.adapter = userAdapter
        homeBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // userViewModel.deleteAll()
        userViewModel.users.observe(viewLifecycleOwner) { users ->
            Log.d("TAG" , "onCreateView: $users")
            userList.clear()
            userList.addAll(users)
            userAdapter.setUsers(users) // Update your adapter with the user list
        }
        // To fetch users when needed, like in onCreate or onStart
        userViewModel.fetchAllUsers()


        homeBinding.addFab.setOnClickListener {
            findNavController().navigate(R.id.noteFragment)
        }
        homeBinding.searchBar.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelableArrayList("userList", userList)  // Convert MutableList to ArrayList
            }
            findNavController().navigate(R.id.searchFragment,bundle)
        }

        homeBinding.ivShuffle.setOnClickListener {
            toggleLayoutManager()
        }
       return homeBinding.root
    }
    private fun toggleLayoutManager() {
        if (isLinearLayout) {
            // Switch to StaggeredGridLayoutManager
            homeBinding.recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            homeBinding.ivShuffle.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_grid_view))
        } else {
            homeBinding.ivShuffle.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_list))
            // Switch back to LinearLayoutManager
            homeBinding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        }

        // Toggle layout type
        isLinearLayout = !isLinearLayout
    }
    override fun editTheUser(position: Int, user: User) {
       /* val bundle = Bundle().apply {
            putString("User",user.toString())
        }*/
        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(user)
        findNavController().navigate(action)
      //  findNavController().navigate(R.id.editNoteFragment,bundle)
        Toast.makeText(requireContext(), "Edit ${user.noteTitle}", Toast.LENGTH_SHORT).show()
    }

    override fun deleteTheUser(position : Int , user : User) {
        //userViewModel.deleteAll(user)
        showSnackBar(
                context = requireContext(),
                view = homeBinding.rootView,
                message = "Item deleted",
                actionText = "UNDO",
                actionListener = {
                   userViewModel.insert(user)
                    // Code to undo the delete action
                }
                    )
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.textView)
        registerForContextMenu(textView)
    }
    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        if (v is TextView) {
            menu.setHeaderTitle("Select Action")

            val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val hasText = v.text.isNotEmpty()
            val hasClipboardText = clipboard.hasPrimaryClip() && clipboard.primaryClipDescription?.hasMimeType(
                    ClipDescription.MIMETYPE_TEXT_PLAIN) == true

            if (hasText) {
                menu.add(0, v.id, 0, "Copy")  // Show "Copy" if there's text in TextView
            }

            if (hasClipboardText) {
                menu.add(0, v.id, 1, "Paste")  // Show "Paste" if clipboard has text content
            }
        }
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val textView = view?.findViewById<TextView>(R.id.textView) ?: return super.onContextItemSelected(item)
        val clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        return when (item.title) {
            "Copy" -> {
                // Copy text to clipboard
                val textToCopy = textView.text.toString()
                val clip = ClipData.newPlainText("label", textToCopy)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(requireContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show()
                true
            }
            "Paste" -> {
                // Paste text from clipboard if available
                val pasteData = clipboard.primaryClip?.getItemAt(0)?.text
                if (!pasteData.isNullOrEmpty()) {
                    textView.text = pasteData
                    Toast.makeText(requireContext(), "Text pasted", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Clipboard is empty", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }



}