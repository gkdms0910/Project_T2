package com.example.project_t2.roomDB.WSentiment

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_t2.models.Sentiments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

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
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WSentimentDatabase::class.java,
                    "wsentimentdb"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                getDBInstance(context).getWSentimentDao().insertAll(getInitialWSentiments())
                            }
                        }
                    })
                    .build()
                DBInstance = instance
                instance
            }
        }

        private fun getInitialWSentiments(): List<WSentimentEntity> {
            return listOf(
                WSentimentEntity(25.0f, 40.0f, 1.5f, 0.0f, Sentiments.HAPPY, LocalDate.now().minusDays(1).toString(), 14, 1),
                WSentimentEntity(20.0f, 80.0f, 2.5f, 5.0f, Sentiments.SAD, LocalDate.now().minusDays(3).toString(), 15, 1),
                WSentimentEntity(22.0f, 55.0f, 2.0f, 0.0f, Sentiments.TENDER, LocalDate.now().minusDays(5).toString(), 13, 1),
                WSentimentEntity(18.0f, 90.0f, 8.0f, 15.0f, Sentiments.ANGRY, LocalDate.now().minusDays(10).toString(), 18, 1),
                WSentimentEntity(15.0f, 50.0f, 6.0f, 0.0f, Sentiments.FEAR, LocalDate.now().minusDays(15).toString(), 11, 1),
                WSentimentEntity(-2.0f, 85.0f, 1.8f, 1.0f, Sentiments.HAPPY, LocalDate.now().minusDays(20).toString(), 16, 1),
                WSentimentEntity(17.0f, 95.0f, 1.0f, 0.0f, Sentiments.TENDER, LocalDate.now().minusDays(22).toString(), 8, 1),
                WSentimentEntity(25.0f, 40.0f, 1.5f, 0.0f, Sentiments.HAPPY, LocalDate.now().minusDays(25).toString(), 17, 1),
                WSentimentEntity(22.0f, 55.0f, 2.0f, 0.0f, Sentiments.SAD, LocalDate.now().minusDays(30).toString(), 19, 1),
                WSentimentEntity(18.0f, 90.0f, 8.0f, 15.0f, Sentiments.FEAR, LocalDate.now().minusDays(35).toString(), 22, 1),
                WSentimentEntity(15.0f, 50.0f, 6.0f, 0.0f, Sentiments.HAPPY, LocalDate.now().minusDays(40).toString(), 14, 1),
                WSentimentEntity(25.0f, 40.0f, 1.5f, 0.0f, Sentiments.TENDER, LocalDate.now().minusDays(45).toString(), 16, 1),
                WSentimentEntity(25.0f, 40.0f, 1.5f, 0.0f, Sentiments.SAD, LocalDate.now().minusDays(2).toString(), 17, 1),
                WSentimentEntity(20.0f, 80.0f, 2.5f, 5.0f, Sentiments.ANGRY, LocalDate.now().minusDays(4).toString(), 13, 1),
                WSentimentEntity(15.0f, 50.0f, 6.0f, 0.0f, Sentiments.FEAR, LocalDate.now().minusDays(6).toString(), 18, 1),
                WSentimentEntity(22.0f, 55.0f, 2.0f, 0.0f, Sentiments.TENDER, LocalDate.now().minusDays(8).toString(), 12, 1),
                WSentimentEntity(18.0f, 90.0f, 8.0f, 15.0f, Sentiments.FEAR, LocalDate.now().minusDays(12).toString(), 23, 1),
                WSentimentEntity(-2.0f, 85.0f, 1.8f, 1.0f, Sentiments.HAPPY, LocalDate.now().minusDays(18).toString(), 10, 1),
                WSentimentEntity(17.0f, 95.0f, 1.0f, 0.0f, Sentiments.SAD, LocalDate.now().minusDays(28).toString(), 7, 1),
                WSentimentEntity(25.0f, 40.0f, 1.5f, 0.0f, Sentiments.HAPPY, LocalDate.now().minusMonths(1).minusDays(1).toString(), 14, 1),
                WSentimentEntity(20.0f, 80.0f, 2.5f, 5.0f, Sentiments.TENDER, LocalDate.now().minusMonths(1).minusDays(7).toString(), 16, 1),
                WSentimentEntity(22.0f, 55.0f, 2.0f, 0.0f, Sentiments.SAD, LocalDate.now().minusMonths(1).minusDays(14).toString(), 15, 1),
                WSentimentEntity(25.0f, 40.0f, 1.5f, 0.0f, Sentiments.HAPPY, LocalDate.now().minusMonths(1).minusDays(21).toString(), 13, 1),
                WSentimentEntity(15.0f, 50.0f, 6.0f, 0.0f, Sentiments.FEAR, LocalDate.now().minusMonths(1).minusDays(28).toString(), 20, 1)
            )
        }
    }
}