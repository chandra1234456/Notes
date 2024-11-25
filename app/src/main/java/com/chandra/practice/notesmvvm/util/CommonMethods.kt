package com.chandra.practice.notesmvvm.util

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun showSnackBar(
    context: Context,
    view: View,
    message: String,
    actionText: String? = null,
    actionListener: (() -> Unit)? = null
                ) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

    // If there is an action, set it
    if (!actionText.isNullOrEmpty() && actionListener != null) {
        snackBar.setAction(actionText) {
            actionListener()
        }
    }

    snackBar.show()
}

fun formatTimestamp(timestamp: String): String {
    val dateFormat = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
    return dateFormat.format(Date(timestamp))
}
