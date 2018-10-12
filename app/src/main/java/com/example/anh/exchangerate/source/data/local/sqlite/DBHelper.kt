package com.example.anh.exchangerate.source.data.local.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.anh.exchangerate.source.model.Currency
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null,
    DATABASE_VERSION) {
  private val mContext: Context = context
  private var db: SQLiteDatabase? = null

  init {
    val dbExist = checkDB()
    if (dbExist) {

    } else {
      this.readableDatabase
      try {
        close()
        copyDB()
      } catch (e: java.lang.Exception) {
        throw Error("Error copying DB")

      }
    }
  }

  override fun onCreate(db: SQLiteDatabase?) {
  }

  override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
  }

  fun getAllCurrency(): MutableList<Currency> {
    val currencyList = mutableListOf<Currency>()
    var cursor: Cursor?
    try {
      db = this.readableDatabase
      cursor = db!!.rawQuery("SELECT * FROM currency", null)
    } catch (e: java.lang.Exception) {
      Log.e(DATABASE_NAME, e.message)
      return currencyList
    }
    if (cursor.moveToFirst()) {
      while (!cursor.isAfterLast()) {
        val currency = Currency()
        currency.id = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_ID))
        currency.currencyName = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_NAME))
        currency.currencySymbol = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_SYMBOL))
        currency.nationName = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_NATION_NAME))
        currencyList.add(currency)
        cursor.moveToNext()
      }
    }
    return currencyList
  }

  fun getAllFavorite(id: String): Currency? {
    var currency = Currency()
    var cursor: Cursor?
    try {
      db = this.readableDatabase
      cursor = db!!.rawQuery(
          "SELECT * FROM favorite FULL JOIN currency ON favorite.id = currency.id AND favorite.id=?",
          arrayOf(id))
    } catch (e: java.lang.Exception) {
      Log.e(DATABASE_NAME, e.message)
      return null
    }
    if (cursor.moveToFirst()) {
      currency.id = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_ID))
      currency.currencyName = cursor.getString(
          cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_NAME))
      currency.currencySymbol = cursor.getString(
          cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_SYMBOL))
      currency.nationName = cursor.getString(
          cursor.getColumnIndex(DBContract.Currency.COLUMN_NATION_NAME))
    }
    return currency
  }

  fun getAllFavorites(): MutableList<Currency> {
    val currencyList = mutableListOf<Currency>()
    var cursor: Cursor?
    try {
      db = this.readableDatabase
      cursor = db!!.rawQuery("SELECT * FROM currency,favorite WHERE currency.id = favorite.id",
          null)
    } catch (e: java.lang.Exception) {
      Log.e(DATABASE_NAME, e.message)
      return currencyList
    }
    if (cursor.moveToFirst()) {
      while (!cursor.isAfterLast()) {
        val currency = Currency()
        currency.id = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_ID))
        currency.currencyName = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_NAME))
        currency.currencySymbol = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_SYMBOL))
        currency.nationName = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_NATION_NAME))
        currencyList.add(currency)
        cursor.moveToNext()
      }
    }
    return currencyList
  }

  fun addFavorite(id: String): Boolean {
    return try {
      db = this.writableDatabase
      db!!.insert("favorite", null, ContentValues().apply { put("id", id) }) > 0
    } catch (e: java.lang.Exception) {
      Log.e(DATABASE_NAME, e.message)
      false
    }
  }

  fun delFavorite(id: String): Boolean {
    return try {
      db = this.readableDatabase
      db!!.delete("favorite", " WHERE favorite.id = " + id, null) > 0
    } catch (e: java.lang.Exception) {
      Log.e(DATABASE_NAME, e.message)
      false
    }
  }

  fun getBaseCurrency(vararg id: String): MutableList<Currency> {
    val currencyList = mutableListOf<Currency>()
    var cursor: Cursor?
    try {
      db = this.writableDatabase
      cursor = db!!.rawQuery("SELECT * FROM currency WHERE id IN(?, ?)", arrayOf(id[0], id[1]))
    } catch (e: Exception) {
      Log.e(DATABASE_NAME, e.message)
      return currencyList
    }
    if (cursor.moveToFirst()) {
      while (!cursor.isAfterLast()) {
        val currency = Currency()
        currency.id = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_ID))
        currency.currencyName = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_NAME))
        currency.currencySymbol = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_SYMBOL))
        currency.nationName = cursor.getString(
            cursor.getColumnIndex(DBContract.Currency.COLUMN_NATION_NAME))
        currencyList.add(currency)
        cursor.moveToNext()
      }
    }
    return currencyList
  }

  @Throws(IOException::class)
  private fun copyDB() {
    val dbInput = mContext.getAssets().open(DATABASE_NAME)
    val outFile = DB_PATH + DATABASE_NAME
    val dbOutput = FileOutputStream(outFile)

    val buffer = ByteArray(1024)
    var length = dbInput.read(buffer)
    while (length > 0) {
      dbOutput.write(buffer, 0, length)
      length = dbInput.read(buffer)
    }
    dbOutput.flush()
    dbOutput.close()
    dbInput.close()
  }

  private fun checkDB(): Boolean {
    var check = false
    try {
      val myPath = DB_PATH + DATABASE_NAME
      val dbfile = File(myPath)
      check = dbfile.exists()
    } catch (e: SQLiteException) {
      // TODO: handle exception
    }
    return check
  }

  fun openDB() {
    val dbPath = DB_PATH + DATABASE_NAME
    db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
  }

  @Synchronized
  override fun close() {
    if (db != null)
      db!!.close()
    super.close()
  }

  companion object {
    // If you change the database schema, you must increment the database version.
    const val DATABASE_VERSION = 4
    const val DATABASE_NAME = "exchangerate.db"
    const val DB_PATH = "/data/data/com.example.anh.exchangerate/databases/"
    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE " + DBContract.Currency.COLUMN_TABLE_NAME + " (" +
            DBContract.Currency.COLUMN_CURRENCY_ID + " TEXT PRIMARY KEY," +
            DBContract.Currency.COLUMN_CURRENCY_NAME + " TEXT," +
            DBContract.Currency.COLUMN_CURRENCY_SYMBOL + " TEXT," +
            DBContract.Currency.COLUMN_NATION_NAME + " TEXT)"

    private var creatDB: DBHelper? = null
    @Synchronized
    fun getHelper(context: Context): DBHelper {
      if (creatDB == null)
        creatDB = DBHelper(context)
      return creatDB as DBHelper
    }
  }
}
