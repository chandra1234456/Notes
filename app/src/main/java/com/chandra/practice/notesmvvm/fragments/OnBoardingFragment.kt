package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.findNavController
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.databinding.FragmentOnBoardingBinding
import com.chandra.practice.notesmvvm.util.tryCatch

class OnBoardingFragment : Fragment() {
    private lateinit var onBoardingBinding : FragmentOnBoardingBinding
    override fun onCreateView(
        inflater : LayoutInflater , container : ViewGroup? ,
        savedInstanceState : Bundle? ,
                             ) : View {
        onBoardingBinding = FragmentOnBoardingBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(onBoardingBinding.root) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemInsets.left,
                systemInsets.top,
                systemInsets.right,
                systemInsets.bottom
            )
            insets
        }
        return onBoardingBinding.root
    }

    override fun onViewCreated(view : View , savedInstanceState : Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        onBoardingBinding.btnContinue.setOnClickListener {
           /* tryCatch(tag = "TAG", onError = { e ->
                Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            }) {
                throw RuntimeException("Exception")
            }*/
              moveToHomeFragment()
        }

    }
    private fun moveToHomeFragment() {
        findNavController().navigate(R.id.homeFragment)
    }

}