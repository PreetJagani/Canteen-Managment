package com.canteenmanagment.ui.CartFoodList

import android.content.Context
import android.os.Bundle
import android.view.View.OnTouchListener
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
import com.shreyaspatil.EasyUpiPayment.EasyUpiPayment
import com.shreyaspatil.EasyUpiPayment.listener.PaymentStatusListener
import com.shreyaspatil.EasyUpiPayment.model.TransactionDetails
import kotlinx.coroutines.launch
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*


class CartFoodList : BaseActivity(), PaymentStatusListener {

    private lateinit var cartFoodListViewModel: CartFoodListViewModel
    private lateinit var binding: ActivityCartFoodListBinding
    private lateinit var addCartCustomDialog: AddCartCustomDiolog
    private var cartFoodList: MutableList<CartFood> = mutableListOf()
    private var mContext: Context = this
    private val customProgressBar: CustomProgressBar = CustomProgressBar(this)
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
            //payUsingUpi(calculateTotalAmount(cartFoodList).toString())
            placeOrder("469204901")
        }



    }

    private fun placeOrder(transactionId: String) {
        customProgressBar.startDialog()
        scope.launch {
            FirebaseApiManager.placeOrderInSystem(cartFoodList, transactionId).let {
                customProgressBar.stopDiaolog()
                if (it.isSuccess) {
                    val editor = mContext.getSharedPreferences(FoodListActivity.CART, 0x0000).edit()
                    editor.remove(FoodListActivity.CART_ITEMS)
                    editor.apply()
                    finish()
                    showShortToast(mContext, "Order place successfully")
                } else
                    showShortToast(mContext, it.message)
            }
        }
    }

    private fun getDataFromSharedPreferences() {

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

    private fun payUsingUpi(amount: String) {
        val name = "Canteen Admin"
        val upiId = "shaileshjagani9825@okicici"
        //val upiId = "preetjagani@oksbi"
        val userName = FirebaseAuth.getInstance().currentUser?.displayName

        val cal: Calendar = Calendar.getInstance()
        val f = SimpleDateFormat("YYYYMMddHHmmss")
        val id = f.format(cal.time)
        val tID = "T{$id}CM"
        val rID = "R{$id}CM"

        val easyUpiPayment = EasyUpiPayment.Builder()
            .with(this)
            .setPayeeVpa(upiId)
            .setPayeeName(name)
            .setTransactionId(tID)
            .setTransactionRefId(rID)
            .setDescription(userName!!)
            .setAmount("$amount.00")
            .build()

        easyUpiPayment.startPayment()
        easyUpiPayment.setPaymentStatusListener(this)

        /*val uri: Uri = Uri.parse("upi://pay")
            .buildUpon()
            .appendQueryParameter("pa", upiId) // virtual ID
            .appendQueryParameter("pn", name) // name
            .appendQueryParameter("tn", userName) // any note about payment
            .appendQueryParameter("am", amount) // amount
            .appendQueryParameter("cu", "INR") // currency
            .build()

        val GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        intent.setPackage(GOOGLE_PAY_PACKAGE_NAME)
        startActivityForResult(intent, UPI_PAYMENT)*/


    }

    private fun calculateTotalAmount(cartFoodList: MutableList<CartFood>): Int {

        var total = 0
        for (cartFood in cartFoodList)
            total += cartFood.quantity * cartFood.food.price!!

        return total
    }

    override fun onTransactionCompleted(transactionDetails: TransactionDetails?) {
        if(transactionDetails?.status.equals("SUCCESS")){
            showShortToast(mContext,"Transaction completed")
            transactionDetails?.transactionId?.let { placeOrder(transactionId = it) }
        }
    }

    override fun onTransactionSuccess() {
        showShortToast(mContext,"Transaction success")
    }

    override fun onTransactionSubmitted() {
        showShortToast(mContext,"Transaction Submited")
    }

    override fun onTransactionFailed() {
        showShortToast(mContext,"Transaction fail")
    }

    override fun onTransactionCancelled() {
        showShortToast(mContext,"Transaction cancel by user")
    }

    override fun onAppNotFound() {
        showShortToast(mContext,"No Upi App found")
    }
/*

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("main ", "response $resultCode")
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
                placeOrder(approvalRefNo)
                Log.e("UPI", "payment successfull: $approvalRefNo")
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
*/

}
