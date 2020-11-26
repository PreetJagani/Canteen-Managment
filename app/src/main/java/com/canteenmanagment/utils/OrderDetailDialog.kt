package com.canteenmanagment.utils

import android.R.attr.bitmap
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.canteenmanagment.R
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.order_detail_diaolog_box.view.*


class OrderDetailDialog(val activity: Activity) {

    lateinit var alertDialog : Dialog

    fun startDialog(orderId: String,transactionId : String){

        var dialog = AlertDialog.Builder(activity)

        var view = LayoutInflater.from(activity).inflate(R.layout.order_detail_diaolog_box, null)

        dialog.setView(view)

        view.TV_transaction_id.text = "Transaction id : $transactionId"

        val qrgEncoder = QRGEncoder(orderId, null, QRGContents.Type.TEXT, 900)
        try {
            // Getting QR-Code as Bitmap
            var bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            view.IM_qr_code.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            Log.v("qr code error : ", e.toString())
        }


        alertDialog = dialog.create()
        alertDialog.show()
        alertDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent)

    }
    fun stopDialog(){
        alertDialog.dismiss()
    }
}