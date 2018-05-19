package com.example.anh.exchangerate.source.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import android.util.Log
import com.example.anh.exchangerate.source.data.NationDatabase.Companion.DATABASE_VERSION
import com.example.anh.exchangerate.source.model.Nation
import java.io.FileOutputStream
import java.io.IOException

@Database(entities = arrayOf(Nation::class), version = DATABASE_VERSION)
abstract class NationDatabase : RoomDatabase() {
    companion object {
        private val TAG = NationDatabase::class.java!!.getSimpleName()
        private var sNationDatabase: NationDatabase? = null

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "exchangerate"

        fun getInstance(context: Context): NationDatabase {
            if (sNationDatabase == null) {
                copyAttachedDatabase(context, this.DATABASE_NAME);
                sNationDatabase = Room.databaseBuilder(context, NationDatabase::class.java, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return sNationDatabase as NationDatabase
        }

        private fun copyAttachedDatabase(context: Context, databaseName: String) {
            val dbPath = context.getDatabasePath(databaseName)
            if (dbPath.exists()) {
                return
            }
            dbPath.parentFile.mkdirs()
            val value: Any = try {
                val inputStream = context.assets.open(databaseName)
                val output = FileOutputStream(dbPath)
                val buffer = ByteArray(8192)
                var length: Int = inputStream.read(buffer, 0, 8192)
                while (length > 0) {
                    output.write(buffer, 0, length)
                }
                output.flush()
                output.close()
                inputStream.close()
            } catch (e: IOException) {
                Log.d(TAG, "Failed to open file", e)
                e.printStackTrace()
            }

        }
    }
}

