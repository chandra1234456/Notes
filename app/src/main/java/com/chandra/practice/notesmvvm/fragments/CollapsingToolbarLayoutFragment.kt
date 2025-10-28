package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.chandra.practice.notesmvvm.R
import com.chandra.practice.notesmvvm.databinding.FragmentCollapsingToolbarLayoutBinding

class CollapsingToolbarLayoutFragment : Fragment() {
    private lateinit var collapsingToolbarLayoutBinding: FragmentCollapsingToolbarLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        collapsingToolbarLayoutBinding =
            FragmentCollapsingToolbarLayoutBinding.inflate(layoutInflater)
        return collapsingToolbarLayoutBinding.root
    }

}