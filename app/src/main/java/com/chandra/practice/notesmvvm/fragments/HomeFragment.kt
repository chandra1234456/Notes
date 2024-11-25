package com.chandra.practice.notesmvvm.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() , UserAdapter.OnItemClickLister {
    private lateinit var userViewModel : UserViewModel
    private lateinit var userAdapter : UserAdapter
    private lateinit var homeBinding : FragmentHomeBinding
    private  var userList: ArrayList<User> = ArrayList()
    private var isLinearLayout = true // To keep track of layout state

    @OptIn(DelicateCoroutinesApi::class)
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
            GlobalScope.launch(Dispatchers.IO){
                Log.d("TAG" , "onCreateView: $users")
                userList.clear()
                withContext(Dispatchers.Main) {
                    userList.addAll(users)
                    userAdapter.setUsers(users) // Update your adapter with the user list
                }
            }
        }
        // To fetch users when needed, like in onCreate or onStart
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                userViewModel.fetchAllUsers()
            }
        }


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
        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(user)
        findNavController().navigate(action)
        Toast.makeText(requireContext(), "Edit", Toast.LENGTH_SHORT).show()
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

}