package com.example.anh.exchangerate.source

interface CallBack<T> {
  fun onSuccess(data: T)
  fun onFailure(mes: String?)
}