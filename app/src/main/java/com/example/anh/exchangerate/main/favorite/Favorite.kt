package com.example.anh.exchangerate.main.favorite

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.FavoriteBinding

class Favorite : Fragment() {
    private lateinit var mFavoriteViewModel: FavoriteViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val favoriteBinding: FavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.favorite, container, false)
        favoriteBinding.viewModel = mFavoriteViewModel
        setHasOptionsMenu(true)
        return favoriteBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFavoriteViewModel = FavoriteViewModel(this.context!!)
    }
}