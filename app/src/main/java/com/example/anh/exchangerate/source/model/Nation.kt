package com.example.anh.exchangerate.source.model

import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull

class Nation() : Parcelable {
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @NonNull
    lateinit var id: String
    lateinit var currencyId: String
    lateinit var currencyName: String
    lateinit var name: String
    lateinit var alpha3: String
    lateinit var currencySymbol: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        currencyId = parcel.readString()
        currencyName = parcel.readString()
        name = parcel.readString()
        alpha3 = parcel.readString()
        currencySymbol = parcel.readString()
    }

    constructor(mId: String, mCurrencyId: String, mCurrencyName: String, mName: String, mAlpha3: String, currencySymbol: String) : this() {
        this.id = mId
        this.currencyId = mCurrencyId
        this.currencyName = mCurrencyName
        this.name = mName
        this.alpha3 = mAlpha3
        this.currencySymbol = currencySymbol
    }

    companion object CREATOR : Parcelable.Creator<Nation> {
        override fun createFromParcel(parcel: Parcel): Nation {
            return Nation(parcel)
        }

        override fun newArray(size: Int): Array<Nation?> {
            return arrayOfNulls(size)
        }
    }
}