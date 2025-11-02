package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.chandra.practice.notesmvvm.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}