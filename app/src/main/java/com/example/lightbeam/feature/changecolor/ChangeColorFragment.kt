package com.example.lightbeam.feature.changecolor

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.navigation.findNavController
import com.example.lightbeam.R
import com.example.lightbeam.Storage
import com.example.lightbeam.databinding.FragmentChangeColorBinding
import com.example.lightbeam.databinding.FragmentFlashlightBinding

class ChangeColorFragment : Fragment(R.layout.fragment_change_color) {
    private lateinit var binding: FragmentChangeColorBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentChangeColorBinding.bind(view)
        changeColors(Storage.isFlashlight)

        with(binding) {
            ibRed.setOnClickListener {
                writeInSharedPreferences(R.color.red)
            }
            ibBlue.setOnClickListener {
                writeInSharedPreferences(R.color.blue)

            }
            ibGreen.setOnClickListener {
                writeInSharedPreferences(R.color.green)
            }
        }
    }

    private fun writeInSharedPreferences(color: Int) {
        val sharedPref =
            activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(getString(R.string.icon_color), color)
            apply()
        }
        view?.findNavController()?.popBackStack()
    }

    private fun changeColors(isFlashlight: Boolean) {
        with(binding) {
            if (isFlashlight) {
                layout.setBackgroundResource(R.color.white)
                tvChangeColor.setTextColor(resources.getColor(R.color.black, null))
            } else {
                layout.setBackgroundResource(R.color.black)
                tvChangeColor.setTextColor(resources.getColor(R.color.white, null))
            }
        }
    }
}