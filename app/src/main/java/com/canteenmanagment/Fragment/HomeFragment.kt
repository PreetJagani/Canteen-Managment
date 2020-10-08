package com.canteenmanagment.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.canteenmanagment.R
import com.canteenmanagment.databinding.FragmentHomeBinding
import com.canteenmanagment.databinding.FragmentProfileBinding
import com.canteenmanagment.ui.CartFoodList.CartFoodList

class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.BTCart.setOnClickListener {
            var i = Intent(context,CartFoodList::class.java)
            startActivity(i)
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }



}