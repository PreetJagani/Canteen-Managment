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
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.FragmentHomeBinding
import com.canteenmanagment.ui.CartFoodList.CartFoodList
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.canteenmanagment.utils.AddCartCustomDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewmodel: HomeFragmentViewModel
    private lateinit var addCartCustomDialog: AddCartCustomDialog
    private lateinit var favFoodList: MutableList<Food>
    val scope = CoroutineScope(Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewmodel = ViewModelProviders.of(this).get(HomeFragmentViewModel::class.java)

        addCartCustomDialog = AddCartCustomDialog(activity as Activity, { food ->  addToFav(food) },{ food ->  removeFromFav(food) })

        viewmodel.foodList.observe(this, Observer {
            if (it.isNotEmpty())
                binding.textView9.visibility = View.VISIBLE

            binding.RVRecentFood.adapter = RecentFoodRecyclerViewAdapter(it) { position ->

                var flag = false
                if(favFoodList.indexOf( it[position] ) != -1)
                    flag = true

                addCartCustomDialog.startDialog(
                    it[position],
                    true,
                    { getDataFromSharedPreferences() },
                    isFavorite = flag,
                    isFavVisible = true
                )
            }
        })

        viewmodel.favFoodList.observe(this, Observer {
            favFoodList = it as MutableList<Food>

            if (it.isNotEmpty())
                binding.textView10.visibility = View.VISIBLE

            binding.RVFav.adapter = FavoriteFoodRecyclerViewAdapter(it) { position ->

                var flag = false
                if(favFoodList.indexOf( it[position] ) != -1)
                    flag = true

                addCartCustomDialog.startDialog(
                    it[position],
                    true,
                    { getDataFromSharedPreferences() },
                    isFavorite = flag,
                    isFavVisible = true
                )
            }

        })

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

    private fun addToFav(food: Food) {
        scope.launch {
            FirebaseApiManager.addFoodToFavourite(food).let {
                if(it.isSuccess)
                    favFoodList.add(food)

            }
        }
    }

    private fun removeFromFav(food: Food) {
        scope.launch {
            FirebaseApiManager.removeFoodFromFavourite(food).let {
                if(it.isSuccess)
                    favFoodList.remove(food)
            }
        }
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