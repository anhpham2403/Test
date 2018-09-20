package com.example.anh.exchangerate.main.exchangeRate

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.ExchangeRateBinding

class ExchangeRate : Fragment() {
    private lateinit var mExchangeRateViewModel: ExchangeRateViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mExchangeRateViewModel = ExchangeRateViewModel(this.context!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val exchangeRateBinding: ExchangeRateBinding = DataBindingUtil.inflate(inflater, R.layout.exchange_rate, container, false)
        exchangeRateBinding.viewModel = mExchangeRateViewModel
        setHasOptionsMenu(true)
        return exchangeRateBinding.root
    }

    override fun onDestroy() {
        mExchangeRateViewModel.onDestroy()
        super.onDestroy()
    }

    companion object {
        fun getInstance(): Fragment {
            return ExchangeRate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.swap -> {
                mExchangeRateViewModel.onChangePrimacy()
                return true
            }
            R.id.reload -> {
                mExchangeRateViewModel.onReload()
                return true
            }
            R.id.chart -> {
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}