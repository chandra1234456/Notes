package com.chandra.practice.notesmvvm.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context , intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule all the alarms based on saved data
            // Loop through your saved reminders and call setReminderNotification() again
        }
    }
}
