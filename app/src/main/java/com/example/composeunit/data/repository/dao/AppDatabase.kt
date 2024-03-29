package com.example.composeunit.data.repository.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.composeunit.data.repository.dao.table.ChatContent
import com.example.composeunit.data.repository.dao.table.ChatContents
import com.example.composeunit.data.repository.dao.table.ChatProgram
import com.example.composeunit.data.repository.dao.table.ChinookDao
import com.example.composeunit.data.repository.dao.table.ComposeData
import com.example.composeunit.data.repository.dao.table.Customers
import com.example.composeunit.data.repository.dao.table.Employees
import com.example.composeunit.data.repository.dao.table.User

@Database(
    entities = [Customers::class, Employees::class, User::class, ComposeData::class, ChatProgram::class, ChatContent::class, ChatContents::class],
    version = 1
)
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
                    "app_database_chinook"
                ).createFromAsset("database/chinook.db")
//                    .addMigrations(object : Migration(0, 1) {
//                        override fun migrate(database: SupportSQLiteDatabase) {
//                            database.execSQL("CREATE INDEX IF NOT EXISTS `index__SupportRepId` ON customers (`SupportRepId`)")
//                            database.execSQL("CREATE INDEX IF NOT EXISTS `index_employees_ReportsTo` ON employees (`ReportsTo`)")
//                        }
//                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}