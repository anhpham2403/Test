package com.example.anh.exchangerate.source.data.local.sqlite

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.anh.exchangerate.source.model.Currency
import java.io.FileOutputStream
import java.io.IOException

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val mContext: Context = context
    private var db: SQLiteDatabase? = null
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
        val dbExist = checkDB()
        if (dbExist) {

        } else {
            this.readableDatabase

            try {
                copyDB()
            } catch (e: Exception) {
                throw Error("Error copying DB")

            }

        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun getAllCurrency(): MutableList<Currency> {
        val currencyList = mutableListOf<Currency>()
        var cursor: Cursor? = null
        try {
            cursor = db!!.rawQuery("SELECT * FROM currency", null)
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, e.message)
            return currencyList
        }
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val currency = Currency()
                currency.id = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_ID))
                currency.currencyName = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_NAME))
                currency.currencySymbol = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_SYMBOL))
                currency.nationName = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_NATION_NAME))
                currencyList.add(currency)
                cursor.moveToNext()
            }
        }
        return currencyList
    }

    fun getBaseCurrency(vararg id: String): MutableList<Currency> {
        val currencyList = mutableListOf<Currency>()
        var cursor: Cursor? = null
        try {
            cursor = db!!.rawQuery("SELECT * FROM currency WHERE id IN(?, ?)", arrayOf(id[0], id[1]))
        } catch (e: Exception) {
            Log.e(DATABASE_NAME, e.message)
            return currencyList
        }
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast) {
                val currency = Currency()
                currency.id = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_ID))
                currency.currencyName = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_NAME))
                currency.currencySymbol = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_CURRENCY_SYMBOL))
                currency.nationName = cursor.getString(cursor.getColumnIndex(DBContract.Currency.COLUMN_NATION_NAME))
                currencyList.add(currency)
                cursor.moveToNext()
            }
        }
        return currencyList
    }

    @Throws(IOException::class)
    private fun copyDB() {
        val dbInput = mContext.assets.open(DBContract.Currency.COLUMN_TABLE_NAME)
        val outFile = DB_PATH + DATABASE_NAME
        val dbOutput = FileOutputStream(outFile)

        val buffer = ByteArray(1024)
        val length = dbInput.read(buffer)
        while (length > 0) {
            dbOutput.write(buffer, 0, length)
        }
        dbOutput.flush()
        dbOutput.close()
        dbInput.close()
    }

    private fun checkDB(): Boolean {
        var check: SQLiteDatabase? = null
        try {
            val dbPath = DB_PATH + DATABASE_NAME
            check = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
        } catch (e: Exception) {
            // TODO: handle exception
        }
        if (check != null) {
            check.close()
        }
        return check != null
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
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "exchangerate.db"
        const val DB_PATH = "/data/data/com.example.anh.exchangerate/databases/"
        private const val SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DBContract.Currency.COLUMN_TABLE_NAME + " (" +
                        DBContract.Currency.COLUMN_CURRENCY_ID + " TEXT PRIMARY KEY," +
                        DBContract.Currency.COLUMN_CURRENCY_NAME + " TEXT," +
                        DBContract.Currency.COLUMN_CURRENCY_SYMBOL + " TEXT," +
                        DBContract.Currency.COLUMN_NATION_NAME + " TEXT)"

        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + DBContract.Currency.COLUMN_TABLE_NAME
    }
}