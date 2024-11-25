package com.chandra.practice.notesmvvm

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user : User)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers() : List<User>

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteNoteById(id: Long)

    @Query("DELETE FROM users")
    suspend fun deleteNoteAll()

    @Query("UPDATE users SET noteTitle = :noteTitle, noteDescription = :noteDescription,isEdited = :isEdited WHERE id = :userId")
    suspend fun updateUserById(userId: Long, noteTitle: String, noteDescription: String,isEdited :Boolean)
}