package com.chandra.practice.notesmvvm

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id : Long = 0 ,
    @ColumnInfo(name = "noteTitle") val noteTitle : String ,
    @ColumnInfo(name = "noteDescription") val noteDescription : String ,
    @ColumnInfo(name = "remainderMe") val remainderMe : String ,
    @ColumnInfo(name = "timestamp") val timeStamp : String ,
    @ColumnInfo(name = "isEdited") val isEdited : Boolean
               ) : Parcelable {

    // Constructor to read data from Parcel
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
            parcel.readLong(),                // Read Long for `id`
            parcel.readString() ?: "",        // Read String for `noteTitle`
            parcel.readString() ?: "",        // Read String for `noteDescription`
            parcel.readString() ?: "",        // Read String for `isImportant`
            parcel.readString() ?: "",       // Read String for `timeStamp`
            parcel.readBoolean()        // Read String for `isEdited`
                                      )

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel , flags: Int) {
        parcel.writeLong(id)               // Write Long for `id`
        parcel.writeString(noteTitle)      // Write String for `noteTitle`
        parcel.writeString(noteDescription) // Write String for `noteDescription`
        parcel.writeString(remainderMe)     // Write String for `isImportant`
        parcel.writeString(timeStamp)       // Write String for `timeStamp`
        parcel.writeBoolean(isEdited)       // Write String for `isEdited`
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
