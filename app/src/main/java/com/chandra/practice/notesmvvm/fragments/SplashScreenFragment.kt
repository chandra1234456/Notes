package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {
    private lateinit var splashScreenBinding : FragmentSplashScreenBinding

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        splashScreenBinding = FragmentSplashScreenBinding.inflate(inflater , container , false)
        Handler().postDelayed({
            // Navigate to the main fragment or activity after delay
            moveToHomeFragment()
        }, 4000) // 3000 milliseconds = 3 seconds
        return splashScreenBinding.root
    }



    private fun moveToHomeFragment() {
        findNavController().navigate(R.id.homeFragment)
    }
}
