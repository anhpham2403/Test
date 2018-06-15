package com.example.anh.exchangerate.choosecurrency

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ChooseCurrencyBinding

class ChooseCurrency : AppCompatActivity() {
    private lateinit var mViewModel: ChooseCurrencyViewModel

    companion object {
        @JvmStatic
        fun getInstance(context: Context): Intent {
            val intent = Intent(context, ChooseCurrency::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ChooseCurrencyViewModel(this)
        val binding: ChooseCurrencyBinding = DataBindingUtil.setContentView(this, R.layout.choose_currency)
        binding.viewModel = mViewModel
    }

    override fun onDestroy() {
        mViewModel.onDestroy()
        super.onDestroy()
    }
}