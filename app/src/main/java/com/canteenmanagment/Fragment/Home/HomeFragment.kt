package com.canteenmanagment.Fragment.Home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canteenmanagment.databinding.FragmentHomeBinding
import com.canteenmanagment.ui.CartFoodList.CartFoodList
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.canteenmanagment.utils.AddCartCustomDiolog

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: HomeFragmentViewModel
    private lateinit var addCartCustomDiolog : AddCartCustomDiolog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)

        viewmodel.foodList.observe(this, Observer {
            if (it.isNotEmpty())
                binding.textView9.visibility = View.VISIBLE

            binding.RVRecentFood.adapter = RecentFoodRecyclerViewAdapter(it) { position ->
                addCartCustomDiolog.startDialog(
                    it[position],
                    true
                ) { getDataFromSharedPreferences() }
            }
        })

        addCartCustomDiolog = AddCartCustomDiolog(activity as Activity)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        getDataFromSharedPreferences()

        binding.BTOrderList.setOnClickListener {
            val i = Intent(context, CartFoodList::class.java)
            startActivity(i)
        }

        return binding.root
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun getDataFromSharedPreferences() {

        val preference = context?.getSharedPreferences(FoodListActivity.CART, 0x0000)
        val cartItemString = preference?.getString(FoodListActivity.CART_ITEMS, null)

        if (cartItemString != null && cartItemString != "[]") {
            binding.BTOrderList.visibility = View.VISIBLE
        } else {
            binding.BTOrderList.visibility = View.INVISIBLE
        }
    }


}