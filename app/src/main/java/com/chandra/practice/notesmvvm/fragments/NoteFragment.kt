package com.chandra.practice.notesmvvm.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.chandra.practice.notesmvvm.AppDatabase
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.User
import com.chandra.practice.notesmvvm.UserRepository
import com.chandra.practice.notesmvvm.UserViewModel
import com.chandra.practice.notesmvvm.UserViewModelFactory
import com.chandra.practice.notesmvvm.databinding.FragmentNoteBinding
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date


class NoteFragment : Fragment() {
    private lateinit var userViewModel : UserViewModel
    private lateinit var noteBinding : FragmentNoteBinding
    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        noteBinding = FragmentNoteBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(noteBinding.root) { view, insets ->
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
        //Date '26/10/2024 02:50:13'
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())
        //fullDate
         val currentDateTimeString = DateFormat.getDateTimeInstance().format(Date())
        noteBinding.tvDate.text = formatDateString(currentDate)
        noteBinding.addNoteFab.setOnClickListener {
            userViewModel.insert(
                    User(
                            noteTitle = noteBinding.tieNoteTitle.text.toString() ,
                            noteDescription = noteBinding.tieNoteDescription.text.toString() ,
                            remainderMe = noteBinding.tvRemainedMe.text.toString() ,
                            timeStamp = currentDateTimeString,
                            isEdited = false
                        )
                                )
            Log.d("", "onCreateView: ${noteBinding.tvRemainedMe.text}")
            Toast.makeText(requireContext() , "Inserted" , Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.homeFragment)
        }
        noteBinding.toolbar.setNavigationOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }
         noteBinding.tvRemainedMe.setOnClickListener {
             showDatePickerDialog()
         }
        return noteBinding.root
    }
    private fun formatDateString(inputDate: String): String {
        // Accepts single- or double-digit day/month
        val inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm:ss")
        val dateTime = LocalDateTime.parse(inputDate, inputFormatter)

        val outputFormatter = DateTimeFormatter.ofPattern("dd MMM, yyyy")
        return dateTime.format(outputFormatter)
    }

    // Show DatePickerDialog
    private fun showDatePickerDialog() {
        // Get the current date
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Create the DatePickerDialog
        val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, selectedYear, selectedMonth, selectedDay ->
                    // After the date is selected, show TimePickerDialog
                    showTimePickerDialog(selectedYear, selectedMonth, selectedDay)
                },
                year, month, dayOfMonth
                                               )

        // Set the minimum date as today
        val today = calendar.timeInMillis
        datePickerDialog.datePicker.minDate = today

        datePickerDialog.show()
    }

    // Show TimePickerDialog after date is selected
    private fun showTimePickerDialog(year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)  // 24-hour format
        val minute = calendar.get(Calendar.MINUTE)

        // Create the TimePickerDialog in 12-hour format (AM/PM)
        val timePickerDialog = TimePickerDialog(
                requireContext(),
                { _, selectedHour, selectedMinute ->
                    // Check if the selected time is in the past
                    if (isTimeInThePast(year, month, dayOfMonth, selectedHour, selectedMinute)) {
                    //    dateTimeTextView.text = "Selected time cannot be in the past!"
                        Log.d("TAG" , "showTimePickerDialog: past")
                        noteBinding.tvRemainedMe.text = "Selected time cannot be in the past!"


                    } else {
                        // Format the selected date and time
                        val selectedDateTime = formatDateTime(year, month, dayOfMonth, selectedHour, selectedMinute)
                        // Display the selected date and time
                        //dateTimeTextView.text = selectedDateTime
                        noteBinding.tvRemainedMe.text = selectedDateTime
                        Log.d("TAG" , "showTimePickerDialog: $selectedDateTime")
                    }
                },
                hour, minute, false  // 'false' means AM/PM (12-hour format)
                                               )
        timePickerDialog.show()
    }

    // Format the selected date and time
    private fun formatDateTime(year: Int, month: Int, day: Int, hour: Int, minute: Int): String {
        // month is zero-based, so add 1 to the month
        val formattedDate = "$day/${month + 1}/$year"

        // Convert the hour to 12-hour format and determine AM/PM
        val amPm = if (hour >= 12) "PM" else "AM"
        val hour12 = if (hour % 12 == 0) 12 else hour % 12
        val formattedTime = "$hour12:${String.format("%02d", minute)} $amPm"

        return "$formattedDate $formattedTime"
       // return "Selected Date: $formattedDate\nSelected Time: $formattedTime"
    }

    // Check if the selected time is in the past compared to the current system time
    private fun isTimeInThePast(year: Int, month: Int, dayOfMonth: Int, hour: Int, minute: Int): Boolean {
        val now = Calendar.getInstance()

        // Set the current date and time to compare with
        val selectedTime = Calendar.getInstance()
        selectedTime.set(year, month, dayOfMonth, hour, minute, 0)

        // If the selected time is before the current time, it's in the past
        return selectedTime.before(now)
    }
   }