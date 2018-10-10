package com.example.anh.exchangerate.source.model

import com.google.gson.Gson


abstract class BaseModel : Cloneable {

    @Throws(CloneNotSupportedException::class)
    public override fun clone(): BaseModel {
        val gson = Gson()
        return gson.fromJson(gson.toJson(this), this.javaClass)
    }
}.