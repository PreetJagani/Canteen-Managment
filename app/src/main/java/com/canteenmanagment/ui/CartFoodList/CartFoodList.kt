package com.canteenmanagment.ui.CartFoodList

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View.OnTouchListener
import android.widget.Toast
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
import com.google.firebase.auth.FirebaseAuth
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
    var TAG = "main"
    val UPI_PAYMENT = 0

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
                addCartCustomDialog.startDialog(cartFoodList.get(position).food, true) {
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


            payUsingUpi(calculateTotalAmount(cartFoodList).toString())

        }

    }

    private fun placeOrder(transactionId : String){
        customProgressBar.startDialog()
            scope.launch {
                FirebaseApiManager.placeOrderInSystem(cartFoodList,transactionId).let {
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

    private fun payUsingUpi(amount: String){

        val name = "Canteen Admin"
        val upiId = "shaileshjagani9825@okicici"
//        val upiId = "9537249836@apl"
        val note = FirebaseAuth.getInstance().currentUser?.displayName

        val uri: Uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        startActivityForResult(intent, UPI_PAYMENT)


    }

    private fun calculateTotalAmount(cartFoodList: MutableList<CartFood>) : Int{

        var total = 0
        for(cartFood in cartFoodList)
            total += cartFood.quantity * cartFood.food.price!!

        return total
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("main ", "response $resultCode")
        when (requestCode) {
            UPI_PAYMENT -> if (RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.e("UPI", "onActivityResult: $trxt")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.e("UPI", "onActivityResult: " + "Return data is null")
                    val dataList: ArrayList<String> = ArrayList()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                //when user simply back without payment
                Log.e("UPI", "onActivityResult: " + "Return data is null")
                val dataList: ArrayList<String> = ArrayList()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(mContext)) {
            var str = data[0]
            Log.e("UPIPAY", "upiPaymentDataOperation: $str")
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=".toRegex()).toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }
            if (status == "success") {
                //Code to handle successful transaction here.
                Toast.makeText(mContext, "Transaction successful.", Toast.LENGTH_SHORT).show()
                Log.e("UPI", "payment successfull: $approvalRefNo")
                placeOrder(approvalRefNo)
            } else if ("Payment cancelled by user." == paymentCancel) {
                Toast.makeText(mContext, "Payment cancelled by user.", Toast.LENGTH_SHORT)
                    .show()
                Log.e("UPI", "Cancelled by user: $approvalRefNo")
            } else {
                Toast.makeText(
                    mContext,
                    "Transaction failed.Please try again",
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("UPI", "failed payment: $approvalRefNo")
            }
        } else {
            Log.e("UPI", "Internet issue: ")
            Toast.makeText(
                mContext,
                "Internet connection is not available. Please check and try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun isConnectionAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val netInfo = connectivityManager.activeNetworkInfo
            if (netInfo != null && netInfo.isConnected
                && netInfo.isConnectedOrConnecting
                && netInfo.isAvailable
            ) {
                return true
            }
        }
        return false
    }
}