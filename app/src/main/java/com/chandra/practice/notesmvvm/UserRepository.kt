package com.chandra.practice.notesmvvm

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepository(private val userDao: UserDao) {

    suspend fun insert(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user)
        }
    }

    suspend fun getAllUsers(): Flow<List<User>> {
        return withContext(Dispatchers.IO) {
            userDao.getAllUsers()
        }
    }

    suspend fun deleteId(user : User) {
        withContext(Dispatchers.IO) {
            userDao.deleteNoteById(user.id)
        }
    }

    suspend fun updateByUserId(id :Long,notTitle:String,noteDescription : String ,isEdited :Boolean) {
        withContext(Dispatchers.IO) {
            userDao.updateUserById(id,notTitle,noteDescription,isEdited)
        }
    }
    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            userDao.deleteNoteAll()
        }
    }
}
