package com.canteenmanagment.ui.UserOrder

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.canteenmanagment.R
import com.canteenmanagment.databinding.ActivityUserOrderBinding
import com.canteenmanagment.utils.OrderDetailDialog

class UserOrder : AppCompatActivity() {

    private lateinit var viewmodel: UserOrderViewModel
    private lateinit var binding: ActivityUserOrderBinding
    private val mContext: Context = this
    private lateinit var orderDetailDialog: OrderDetailDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_order)
        viewmodel = ViewModelProviders.of(this).get(UserOrderViewModel::class.java)
        orderDetailDialog = OrderDetailDialog(this)

        setContentView(binding.root)

        viewmodel.userPastOrderList.observe(this, Observer {
            binding.RVPastOrder.adapter = PastOrderRecyclerViewAdapter(
                viewmodel.getHashmapFromList(it)
            )

        })

        viewmodel.userInProgressOrder.observe(this, Observer {
            binding.RVPreparing.adapter = OnGoingOrderRecyclerViewAdapter(
                viewmodel.getPastOrderListFromOrderList(it),
                mContext
            ) { orderId, transactionId -> orderDetailDialog.startDialog(orderId, transactionId) }
        })
        viewmodel.userReadyOrder.observe(this, Observer {
            binding.RVReady.adapter = OnGoingOrderRecyclerViewAdapter(
                viewmodel.getPastOrderListFromOrderList(it),
                mContext
            ){ orderId, transactionId -> orderDetailDialog.startDialog(orderId, transactionId) }
        })

        binding.IMBack.setOnClickListener {
            super.onBackPressed()
        }

    }
}