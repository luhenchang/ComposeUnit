package com.example.composeunit
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
@Database(entities = [Customers::class, Employees::class,User::class,ComposeData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chinookDao(): ChinookDao
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database_chinook")
                    .createFromAsset("database/chinook.db")
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}