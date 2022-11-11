package com.othadd.hngmobiletask3.util

import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.othadd.hngmobiletask3.CountryRecyclerAdapter
import com.othadd.hngmobiletask3.R

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        imgView.load(imgUri){
            placeholder(R.drawable.loading_animation)
            error(R.drawable.ic_broken_image)
        }
    }
}

@BindingAdapter("dataList")
fun bindDataList(recyclerView: RecyclerView, dataList: List<Any>?){
    dataList?.let {
        (recyclerView.adapter  as CountryRecyclerAdapter).submitList(it)
    }
}