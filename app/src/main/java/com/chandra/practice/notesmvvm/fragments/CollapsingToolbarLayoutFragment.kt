package com.chandra.practice.notesmvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.chandra.practice.notesmvvm.databinding.FragmentCollapsingToolbarLayoutBinding

class CollapsingToolbarLayoutFragment : Fragment() {
    private lateinit var collapsingToolbarLayoutBinding: FragmentCollapsingToolbarLayoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        collapsingToolbarLayoutBinding =
            FragmentCollapsingToolbarLayoutBinding.inflate(layoutInflater)
        ViewCompat.setOnApplyWindowInsetsListener(collapsingToolbarLayoutBinding.root) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemInsets.left,
                systemInsets.top,
                systemInsets.right,
                systemInsets.bottom
            )
            insets
        }
        return collapsingToolbarLayoutBinding.root
    }

}