package com.example.anh.exchangerate.utils

import android.support.v7.widget.RecyclerView
import android.databinding.BindingAdapter
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.example.anh.exchangerate.R


class BindingUtils {
    companion object {
        @BindingAdapter("app:recyclerAdapter")
        @JvmStatic
        fun setAdapterForRecyclerView(recyclerView: RecyclerView,
                                      adapter: RecyclerView.Adapter<*>) {
            recyclerView.adapter = adapter
        }

        @BindingAdapter("layoutManager")
        @JvmStatic
        fun setLayoutManager(recyclerView: RecyclerView,
                             layoutManagerFactory: LayoutManagers.LayoutManagerFactory) {
            recyclerView.layoutManager = layoutManagerFactory.create(recyclerView)
        }

        @BindingAdapter("app:flagCurrency")
        @JvmStatic
        fun setFlagCurrency(image: ImageView, id: String) {
            val id = image.context.resources.getIdentifier(id.toLowerCase(), "drawable", image.context.packageName)
            if (id > 0){
                Glide.with(image).load(id).into(image)
            }
        }
    }
}