package com.canteenmanagment.ui.CartFoodList

import android.content.Context
import android.os.Bundle
import android.view.View.OnTouchListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.CartFood
import com.canteenmanagment.databinding.ActivityCartFoodListBinding
import com.canteenmanagment.helper.showShortToast
import com.canteenmanagment.ui.FoodList.FoodListActivity
import com.canteenmanagment.utils.AddCartCustomDiolog
import com.canteenmanagment.utils.CustomProgressBar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.reflect.Type


class CartFoodList : BaseActivity() {

    private lateinit var cartFoodListViewModel : CartFoodListViewModel
    private lateinit var binding : ActivityCartFoodListBinding
    private lateinit var addCartCustomDialog : AddCartCustomDiolog
    private var cartFoodList : MutableList<CartFood> = mutableListOf()
    private var mContext : Context = this
    private val customProgressBar : CustomProgressBar = CustomProgressBar(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_food_list)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_cart_food_list
        )
        cartFoodListViewModel = ViewModelProviders.of(this).get(CartFoodListViewModel::class.java)

        binding.RVCartFood.setOnTouchListener(OnTouchListener { v, event -> true })

        addCartCustomDialog = AddCartCustomDiolog(this)

        getDataFromSharedPreferences()

        cartFoodListViewModel.cartFoodList.observe(this, Observer {

            binding.RVCartFood.adapter = CartFoodListRecyclerViewAdapter(it) { position ->
                addCartCustomDialog.startDialog(cartFoodList.get(position).food,true) {
                    getDataFromSharedPreferences()
                }
            }

            binding.TVTotal.text = "= ${calculateTotalAmount(it)} Rs."
            cartFoodList = it
        })

        binding.IMBack.setOnClickListener {
            super.onBackPressed()
        }

        binding.BTPlaceOrder.setOnClickListener {
            customProgressBar.startDialog()
            scope.launch {
                FirebaseApiManager.placeOrderInSystem(cartFoodList).let {
                    customProgressBar.stopDiaolog()
                    if(it.isSuccess){
                        val editor = mContext.getSharedPreferences(FoodListActivity.CART, 0x0000).edit()
                        editor.remove(FoodListActivity.CART_ITEMS)
                        editor.apply()
                        finish()
                        showShortToast(mContext,"Order place successfully")
                    }
                    else
                        showShortToast(mContext,it.message)
                }
            }

        }

    }

    private fun getDataFromSharedPreferences(){

        val preference = application.getSharedPreferences(FoodListActivity.CART, 0x0000)

        val gson = Gson()
        val type: Type = object : TypeToken<MutableList<CartFood>?>() {}.type

        val cartItemString = preference.getString(FoodListActivity.CART_ITEMS, null)

        val cartItemLIst = if (cartItemString != null)
            gson.fromJson<MutableList<CartFood>>(cartItemString, type)
        else
            mutableListOf()

        cartFoodListViewModel.cartFoodList.postValue(cartItemLIst)
    }

    private fun calculateTotalAmount(cartFoodList: MutableList<CartFood>) : Int{

        var total = 0
        for(cartFood in cartFoodList)
            total += cartFood.quantity * cartFood.food.price!!

        return total
    }
}