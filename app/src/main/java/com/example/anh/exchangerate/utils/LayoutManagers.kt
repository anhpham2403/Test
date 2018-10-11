package com.example.anh.exchangerate.utils

import android.support.annotation.IntDef
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class LayoutManagers {
  constructor()

  interface LayoutManagerFactory {
    fun create(recyclerView: RecyclerView): RecyclerView.LayoutManager
  }

  @IntDef(LinearLayoutManager.HORIZONTAL, LinearLayoutManager.VERTICAL)
  @Retention(RetentionPolicy.SOURCE)
  annotation class Orientation

  companion object {

    fun linear(): LayoutManagerFactory {
      return object : LayoutManagerFactory {
        override fun create(recyclerView: RecyclerView): RecyclerView.LayoutManager {
          return LinearLayoutManager(recyclerView.context)
        }
      }
    }

    fun linear(@Orientation orientation: Int): LayoutManagerFactory {
      return object : LayoutManagerFactory {
        override fun create(recyclerView: RecyclerView): RecyclerView.LayoutManager {
          return LinearLayoutManager(recyclerView.context, orientation, false)
        }
      }
    }

    @JvmStatic
    fun grid(spanCount: Int): LayoutManagerFactory {
      return object : LayoutManagerFactory {
        override fun create(recyclerView: RecyclerView): RecyclerView.LayoutManager {
          return GridLayoutManager(recyclerView.context, spanCount)
        }
      }
    }

    fun grid(spanCount: Int, @Orientation orientation: Int,
        reverseLayout: Boolean): LayoutManagerFactory {
      return object : LayoutManagerFactory {
        override fun create(recyclerView: RecyclerView): RecyclerView.LayoutManager {
          return GridLayoutManager(recyclerView.context, spanCount, orientation,
              reverseLayout)
        }
      }
    }
  }
}