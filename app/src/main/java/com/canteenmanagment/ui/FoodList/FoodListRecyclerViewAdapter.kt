package com.canteenmanagment.ui.FoodList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.canteenmanagment.R
import com.canteenmanagment.canteen_managment_library.models.Food
import com.canteenmanagment.databinding.ItemFoodListLayoutBinding

class FoodListRecyclerViewAdapter(val foodList : List<Food>, val listner: ClickListner) : RecyclerView.Adapter<FoodListRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFoodListLayoutBinding.inflate(layoutInflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.TVTitle.text = foodList.get(position).name
        holder.binding.TVPrice.text = foodList.get(position).price.toString() + " Rs."

        if(!foodList.get(position).available){
            holder.binding.TVNotAvailable.visibility = View.VISIBLE
        }
        else{
            holder.binding.TVNotAvailable.visibility = View.INVISIBLE
        }

        holder.binding.CL.setOnClickListener {
            listner.openActivity(position)
        }


        Glide.with(holder.binding.root)
            .load(foodList.get(position).imageurl)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .placeholder(R.drawable.error_image)
            .error(R.drawable.error_image)
            .into(holder.binding.IMFoodImage)

    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    class ViewHolder(var binding: ItemFoodListLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    class ClickListner(val openActivity : (position : Int)-> Unit)

}