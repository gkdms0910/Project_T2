package com.example.project_t2.roomDB.WSentiment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [WSentimentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class WSentimentDatabase: RoomDatabase() {
    abstract fun getWSentimentDao(): WSentimentDao

    companion object {
        @Volatile
        private var DBInstance: WSentimentDatabase? = null

        fun getDBInstance(context: Context): WSentimentDatabase {
            return DBInstance ?: synchronized(this) {
                val instance = DBInstance ?: Room.databaseBuilder(
                    context.applicationContext,
                    WSentimentDatabase::class.java,
                    "wsentimentdb"
                ).build()
                DBInstance = instance
                instance
            }
        }
    }
}