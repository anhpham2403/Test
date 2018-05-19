package com.example.anh.exchangerate.source.data

import android.arch.persistence.room.Query
import com.example.anh.exchangerate.source.model.Nation
import io.reactivex.Flowable



interface NationDAO{
    @Query("SELECT * FROM nation")
    fun getNations(): Flowable<List<Nation>>
}