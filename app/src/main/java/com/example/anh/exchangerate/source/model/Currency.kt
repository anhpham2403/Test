package com.example.anh.exchangerate.source.model

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull
import com.android.databinding.library.baseAdapters.BR

class Currency() : Parcelable, BaseObservable() {

    @NonNull
    var id: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.id)
        }
        @Bindable
        get() = field
    var currencyName: String? = null
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencyName)
        }
    var currencySymbol: String? = null
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.currencySymbol)
        }
    var nationName: String? = null
        @Bindable
        get() = field
        set(value) {
            field = value
            notifyPropertyChanged(BR.nationName)
        }

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()
        currencyName = parcel.readString()
        currencySymbol = parcel.readString()
        nationName = parcel.readString()
    }

    constructor(id: String?, currencyName: String?, currencySymbol: String?, nationName: String?) : this() {
        this.id = id
        this.currencyName = currencyName
        this.currencySymbol = currencySymbol
        this.nationName = nationName
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(currencyName)
        parcel.writeString(currencySymbol)
        parcel.writeString(nationName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency {
            return Currency(parcel)
        }

        override fun newArray(size: Int): Array<Currency?> {
            return arrayOfNulls(size)
        }
    }
}