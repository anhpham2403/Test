package com.example.anh.exchangerate

import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4

import org.junit.runner.RunWith

import android.arch.persistence.room.Room
import org.junit.After
import java.io.IOException


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    private var db: NationDatabase
    private var currencyDAO: CurrencyDAO
    init {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(context, NationDatabase::class.java!!).build();
        currencyDAO = db.currencyDAO()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }


}
