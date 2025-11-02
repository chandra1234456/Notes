package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chandra.practice.notesmvvm.AppDatabase
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.UserRepository
import com.chandra.practice.notesmvvm.UserViewModel
import com.chandra.practice.notesmvvm.UserViewModelFactory
import com.chandra.practice.notesmvvm.databinding.FragmentEditNoteBinding

class EditNoteFragment : Fragment() {
   private lateinit var editNoteBinding : FragmentEditNoteBinding
   private lateinit var userViewModel : UserViewModel
   private var userId : Long =0

   override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val editUserItem = arguments?.let { EditNoteFragmentArgs.fromBundle(it).userData }
        Log.d("DATA" , "onViewCreated: $editUserItem")
        userId = editUserItem!!.id
        editNoteBinding.editTvDate.text = editUserItem.timeStamp
        editNoteBinding.tieEditNoteDescription.setText(editUserItem.noteDescription)
        editNoteBinding.tieEditNoteTitle.setText(editUserItem.noteTitle)
        editNoteBinding.editTvRemainedMe.text = editUserItem.remainderMe
    }
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        editNoteBinding = FragmentEditNoteBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(editNoteBinding.root) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemInsets.left,
                systemInsets.top,
                systemInsets.right,
                systemInsets.bottom
            )
            insets
        }
        val dao = AppDatabase.getDatabase(requireContext()).userDao()
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)
        userViewModel = ViewModelProvider(this , factory)[UserViewModel::class.java]

        editNoteBinding.editToolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
        editNoteBinding.saveTheNote.setOnClickListener {
            userViewModel.updateByUserId(userId,editNoteBinding.tieEditNoteTitle.text.toString(),editNoteBinding.tieEditNoteDescription.text.toString(),true)
            Toast.makeText(requireContext() , "Updated" , Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.homeFragment)
        }
        return editNoteBinding.root

    }
}