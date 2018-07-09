package com.example.anh.exchangerate.calculator

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.CalculatorActivityBinding
import com.example.anh.exchangerate.source.model.Currency

class CalculatorActivity : AppCompatActivity() {
    private lateinit var mViewModel: CalculatorViewModel

    companion object {
        fun getInstance(context: Context, isCurrency1: Boolean, currency: Currency): Intent {
            val intent = Intent(context, CalculatorActivity::class.java)
            intent.putExtra("isCurrency1", isCurrency1)
            intent.putExtra("currency", currency)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        var isCurrency1 = intent.getBooleanExtra("isCurrency1", false)
        var currency = intent.getParcelableExtra<Currency>("currency")
        mViewModel = CalculatorViewModel(this, isCurrency1, currency)
        val binding: CalculatorActivityBinding = DataBindingUtil.setContentView(this, R.layout.calculator_activity)
        binding.viewModel = mViewModel
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        mViewModel.onDestroy()
        super.onDestroy()
    }
}
