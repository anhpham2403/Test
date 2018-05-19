package com.example.anh.exchangerate.source.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.NonNull

@Entity(tableName = "nation")
class Nation(): Parcelable{
    override fun writeToParcel(dest: Parcel?, flags: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun describeContents(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private lateinit var mId: String
    @ColumnInfo(name = "currencyId")
    private lateinit var mCurrencyId: String
    @ColumnInfo(name = "currencyName")
    private lateinit var mCurrencyName: String
    @ColumnInfo(name = "name")
    private lateinit var mName: String
    @ColumnInfo(name = "alpha3")
    private lateinit var mAlpha3: String
    @ColumnInfo(name = "currencySymbol")
    private lateinit var mCurrencySymbol: String

    constructor(parcel: Parcel) : this() {
        mId = parcel.readString()
        mCurrencyId = parcel.readString()
        mCurrencyName = parcel.readString()
        mName = parcel.readString()
        mAlpha3 = parcel.readString()
        mCurrencySymbol = parcel.readString()
    }

    constructor(mId: String, mCurrencyId: String, mCurrencyName: String, mName: String, mAlpha3: String,currencySymbol: String) : this() {
        this.mId = mId
        this.mCurrencyId = mCurrencyId
        this.mCurrencyName = mCurrencyName
        this.mName = mName
        this.mAlpha3 = mAlpha3
        this.mCurrencySymbol = currencySymbol
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