package com.canteenmanagment.ui.FoodList

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragments.MenuFragment.Companion.CATEGORY_NAME
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ActivityFoodListBinding
import com.canteenmanagment.ui.CartFoodList.CartFoodList
import com.canteenmanagment.utils.AddCartCustomDiolog
import kotlinx.coroutines.launch


class FoodListActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityFoodListBinding
    private val mContext: Context = this
    private lateinit var foodList: List<Food>
    private lateinit var addCartCustomDiolog : AddCartCustomDiolog
    private var flag = false //flag to determine cart has item or no



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_food_list
        )
        binding.title = intent.getStringExtra(CATEGORY_NAME)

        binding.IMback.setOnClickListener(this)

        loadData()


        addCartCustomDiolog = AddCartCustomDiolog(this)

        binding.SRRefreshLayout.setOnRefreshListener {
            loadData()
        }

        binding.BTOrderList.setOnClickListener {
            var i = Intent(mContext, CartFoodList::class.java)
            startActivity(i)
        }

        /*binding.RVFoodList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (flag && dy > 0 && binding.BTOrderList.visibility === View.VISIBLE) {
                    binding.BTOrderList.visibility = View.INVISIBLE
                } else if (flag && dy < 0 && binding.BTOrderList.visibility === View.INVISIBLE) {
                    binding.BTOrderList.visibility = View.VISIBLE
                }
            }
        })*/

    }

    override fun onStart() {
        super.onStart()
        getDataFromSharedPreferences()
    }

    private fun loadData() {
        scope.launch {
            FirebaseApiManager.getAllFoodFromCategory(intent.getStringExtra(CATEGORY_NAME)).let {
                binding.SRRefreshLayout.isRefreshing = false
                foodList = it
                binding.RVFoodList.visibility = View.VISIBLE
                binding.RVFoodList.adapter = FoodListRecyclerViewAdapter(it,
                    FoodListRecyclerViewAdapter.ClickListner { position ->
                        if (foodList[position].available)
                            addCartCustomDiolog.startDialog(
                                foodList.get(position),
                                true
                            ) { getDataFromSharedPreferences() }
                    }
                )
            }
        }
    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.IMback -> onBackPressed()

        }
    }

    override fun onBackPressed() {
        binding.CL.visibility = View.INVISIBLE
        super.onBackPressed()

    }

    companion object{

        const val CART = "Cart"
        const val CART_ITEMS = "Cart Items"

    }
    private fun getDataFromSharedPreferences(){

        val preference = application.getSharedPreferences(FoodListActivity.CART, 0x0000)
        val cartItemString = preference.getString(FoodListActivity.CART_ITEMS, null)



        if (cartItemString != null && cartItemString != "[]"){
            flag = true
            binding.BTOrderList.visibility = View.VISIBLE
        }
        else{
            flag = false
            binding.BTOrderList.visibility = View.INVISIBLE
        }
    }


}