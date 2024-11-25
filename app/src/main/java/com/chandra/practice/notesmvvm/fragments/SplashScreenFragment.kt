package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.databinding.FragmentSplashScreenBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SplashScreenFragment : Fragment() , CoroutineScope {
    private lateinit var splashScreenBinding : FragmentSplashScreenBinding
    private val job = Job()

    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        splashScreenBinding = FragmentSplashScreenBinding.inflate(inflater , container , false)
        return splashScreenBinding.root
    }

    override val coroutineContext : CoroutineContext
        get() = Dispatchers.Main + job

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        launch {
            delay(5000) // Delay for 3 seconds
            moveToHomeFragment() // Call method to move to home fragment
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        job.cancel() // Cancel coroutines when the view is destroyed
    }

    private fun moveToHomeFragment() {
        findNavController().navigate(R.id.homeFragment)
    }
}
