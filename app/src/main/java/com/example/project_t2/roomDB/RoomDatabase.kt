package com.example.project_t2.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DiaryEntity::class],
    version = 1,
    exportSchema = false
)
//@TypeConverters(Converters::class)
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun getDiaryDao(): DiaryDao

    companion object {
        @Volatile
        private var DBInstance: DiaryDatabase? = null
        fun getDBInstance(context: Context): DiaryDatabase {
            return DBInstance ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DiaryDatabase::class.java,
                    "diarydb"
                ).build()
                DBInstance = instance
                instance
            }
        }
    }
}