package com.chandra.practice.notesmvvm

import android.content.ClipDescription
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>> get() = _users

    fun insert(user: User) {
        viewModelScope.launch {
            repository.insert(user)
            fetchAllUsers() // This fetches all users after inserting
        }
    }

    // Fetches all users without needing a User parameter
    fun fetchAllUsers() {
        viewModelScope.launch {
            _users.value = repository.getAllUsers() // Assuming this returns a List<User>
        }
    }

    fun deleteId(user: User) {
        viewModelScope.launch {
            repository.deleteId(user)
            fetchAllUsers() // Fetches all users after deletion
        }
    }
    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
            fetchAllUsers() // Fetches all users after deletion
        }
    }
    fun updateByUserId(id :Long,noteTitle :String ,noteDescription : String,isEdited :Boolean) {
        viewModelScope.launch {
            repository.updateByUserId(id,noteTitle,noteDescription,isEdited)
            fetchAllUsers() // Fetches all users after deletion
        }
    }

}
