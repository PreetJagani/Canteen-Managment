package com.canteenmanagment.utils

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import com.canteenmanagment.databinding.OrderDetailDiaologBoxBinding
import com.google.zxing.WriterException


class OrderDetailDialog(val activity: Activity) {

    lateinit var alertDialog : Dialog

    fun startDialog(orderId: String,transactionId : String){

        var dialog = AlertDialog.Builder(activity)

        val binding = OrderDetailDiaologBoxBinding.inflate(LayoutInflater.from(activity))

        dialog.setView(binding.root)

        binding.TVTransactionId.text = "Transaction id : $transactionId"

        val qrgEncoder = QRGEncoder(orderId, null, QRGContents.Type.TEXT, 900)
        try {
            // Getting QR-Code as Bitmap
            var bitmap = qrgEncoder.bitmap
            // Setting Bitmap to ImageView
            binding.IMQrCode.setImageBitmap(bitmap)
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