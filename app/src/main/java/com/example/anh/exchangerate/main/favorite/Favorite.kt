package com.example.anh.exchangerate.main.favorite

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import com.example.anh.exchangerate.R
import com.example.anh.exchangerate.databinding.FavoriteBinding

class Favorite : Fragment() {
  private lateinit var mFavoriteViewModel: FavoriteViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    val favoriteBinding: FavoriteBinding = DataBindingUtil.inflate(inflater, R.layout.favorite,
        container, false)
    favoriteBinding.viewModel = mFavoriteViewModel
    setHasOptionsMenu(true)
    return favoriteBinding.root
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mFavoriteViewModel = FavoriteViewModel(this.context!!)
  }

  override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
    inflater!!.inflate(R.menu.chart_menu, menu)
    super.onCreateOptionsMenu(menu, inflater)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    when (item.itemId) {
      R.id.add -> {
        return true
      }
      R.id.sort -> {
        return true
      }
      R.id.reload -> {
        return true
      }
      else -> return super.onOptionsItemSelected(item)
    }
  }
}