package com.chandra.practice.notesmvvm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.chandra.practice.notesmvvm.notification.NotificationReceiver
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class UserAdapter(private var listner : OnItemClickLister,val context : Context) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users: List<User> = listOf()

    interface OnItemClickLister {
        fun deleteTheUser(position : Int,user:User)
        fun editTheUser(position: Int, user: User) // Added for edit action

    }

    // ViewHolder class to hold the views
    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val noteTitle: MaterialTextView = itemView.findViewById(R.id.tvNoteTitle)
        private val noteDescription: MaterialTextView = itemView.findViewById(R.id.tvNoteDescription)
        private val noteDate: MaterialTextView = itemView.findViewById(R.id.tvNoteDate)
        private val noteTimeStamp: MaterialTextView = itemView.findViewById(R.id.tvTimestamp)
        private val ivTimeStamp: ImageView = itemView.findViewById(R.id.ivTimeStamp)
        private val remainderMe: MaterialTextView =itemView.findViewById(R.id.tvRemainder)
        private val cardView: MaterialCardView = itemView.findViewById(R.id.cardViewNote)
        private val isEdited : MaterialTextView = itemView.findViewById(R.id.tvNoteIsEdited)

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(user: User , listner : OnItemClickLister,context : Context) {
            noteTitle.text = user.noteTitle
            noteDescription.text = user.noteDescription
            noteDate.text = extractDate(user.timeStamp)
            Log.d("TAG" , "bind: ${user.remainderMe}")
            remainderMe.apply {
                visibility = if (user.remainderMe == "Remained Me") View.GONE else View.VISIBLE
                text = user.remainderMe
            }
            isEdited.apply {
                visibility = if (user.isEdited) View.VISIBLE else View.GONE
            }
            val text =getTimeGapMessage(user.timeStamp)
            if (text=="Just now"){
                noteTimeStamp.text = getTimeGapMessage(user.timeStamp)
                ivTimeStamp.visibility = View.VISIBLE
            }else{
                ivTimeStamp.visibility = View.GONE
                noteTimeStamp.text = getTimeGapMessage(user.timeStamp)
                noteTimeStamp.setTextColor(context.getColor(R.color.gray))
            }
            cardView.setOnClickListener {
                listner.editTheUser(position = position, user = user)
            }
            // Automatically set the reminder when the data is bound to the view
          //  setReminderNotification(user,context)
        }
        private fun extractDate(input: String): String {
            val datePart = input.split(" ")[0]
            val (day, month) = datePart.split("-") // Split by the dash
            return "$day $month" // Split by space and take the first part
        }
        @RequiresApi(Build.VERSION_CODES.O)
        fun getTimeGapMessage(inputDate: String): String {
            // Define the correct date format
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy h:mm:ss a")
            val providedDateTime = LocalDateTime.parse(inputDate, dateTimeFormatter)
            val currentDateTime = LocalDateTime.now()

            // Calculate the difference
            val minutes = ChronoUnit.MINUTES.between(providedDateTime, currentDateTime)
            val hours = ChronoUnit.HOURS.between(providedDateTime, currentDateTime)

            return when {
                minutes < 1 -> "Just now"
                minutes < 60 -> when (minutes) {
                    1L -> "1 min ago"
                    else -> "$minutes min ago"
                }
                hours < 24 -> when (hours) {
                    1L -> "1 hr ago"
                    else -> "$hours hr ago"
                }
                else -> {
                    when (val days = ChronoUnit.DAYS.between(providedDateTime, currentDateTime)) {
                        1L -> "1 day ago"
                        else -> "$days days ago"
                    }
                }
            }
        }
        fun setReminderNotification(reminder: User,context : Context) {
            // Check if the reminder time is in the future
            val currentTime = System.currentTimeMillis()

            // If the reminder's time is in the future, set the notification
            if (reminder.timeStamp > currentTime.toString()) {
                // Create a notification intent
                val intent = Intent(context , NotificationReceiver::class.java).apply {
                    putExtra("reminderTitle" , reminder.noteTitle)
                    putExtra("reminderTime" , reminder.timeStamp)
                }

                // Create a PendingIntent to trigger the notification
                val pendingIntent = PendingIntent.getBroadcast(
                        context ,
                        reminder.timeStamp.toInt() ,  // Use the reminder time as a unique ID
                        intent ,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                                              )

                // Set the notification using AlarmManager
                val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setExact(AlarmManager.RTC_WAKEUP , reminder.timeStamp.toLong() , pendingIntent)
            }
        }


    }

    // Create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    // Replace the contents of a view
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: UserViewHolder , position: Int) {
        holder.bind(users[position],listner,context)
        Log.d("TAG" , "onBindViewHolder: ${users[position]}")
    }

    // Return the size of the dataset
    override fun getItemCount(): Int {
        return users.size
    }

    // Method to update the list of users
    fun setUsers(userList: List<User>) {
        users = userList
        notifyDataSetChanged()
    }

}
