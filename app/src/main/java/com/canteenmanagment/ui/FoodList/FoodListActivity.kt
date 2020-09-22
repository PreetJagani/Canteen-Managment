package com.canteenmanagment.ui.FoodList

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.canteenmanagment.BaseActivity.BaseActivity
import com.canteenmanagment.Fragments.MenuFragment.Companion.CATEGORY_NAME
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.apiManager.FirebaseApiManager
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ActivityFoodListBinding
import com.canteenmanagment.databinding.AddCartCustomeDiologBinding
import kotlinx.coroutines.launch
import com.canteenmanagment.helper.AddCartCustomDiolog

class FoodListActivity : BaseActivity(), View.OnClickListener {

    lateinit var binding: ActivityFoodListBinding
    private val mContext: Context = this
    private lateinit var foodList: List<Food>
    private lateinit var addCartCustomDiolog : AddCartCustomDiolog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_food_list)

        binding = DataBindingUtil.setContentView(this,
            R.layout.activity_food_list
        )
        binding.title = intent.getStringExtra(CATEGORY_NAME)

        binding.IMback.setOnClickListener(this)

        loadData()

        addCartCustomDiolog = AddCartCustomDiolog(this)


        binding.SRRefreshLayout.setOnRefreshListener {
            loadData()
        }


    }

    private fun loadData() {
        scope.launch {
            FirebaseApiManager.getAllFoodFromCategory(intent.getStringExtra(CATEGORY_NAME)).let {
                binding.SRRefreshLayout.isRefreshing = false
                foodList = it
                binding.RVFoodList.visibility = View.VISIBLE
                binding.RVFoodList.adapter = FoodListRecyclerViewAdapter(it,
                    FoodListRecyclerViewAdapter.ClickListner { position ->
                        if(foodList[position].available)
                            addCartCustomDiolog.startDialog(foodList.get(position))
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


}