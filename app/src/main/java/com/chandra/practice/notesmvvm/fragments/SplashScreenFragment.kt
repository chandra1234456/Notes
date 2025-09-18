package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
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
        /* Handler().postDelayed({
             // Navigate to the main fragment or activity after delay
             moveToHomeFragment()
         }, 4000) //4 seconds*/

        startSplashSequence()
        return splashScreenBinding.root
    }

    private fun moveToHomeFragment() {
        findNavController().navigate(R.id.onBoardingFragment)
    }

    private fun startSplashSequence() {
        splashScreenBinding.imageView.animate()
                .translationY(0f)
                .setDuration(3000)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    splashScreenBinding.colorOverlay.visibility = View.VISIBLE
                    splashScreenBinding.colorOverlay.animate()
                            .alpha(1f)
                            .setDuration(1000)
                            .withEndAction {
                                // Go to main screen
                                moveToHomeFragment()
                            }.start()
                }.start()
    }

}
