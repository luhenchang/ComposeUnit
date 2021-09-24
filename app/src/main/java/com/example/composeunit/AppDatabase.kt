package com.example.composeunit
import androidx.room.Database
import androidx.room.RoomDatabase
@Database(entities = [Customers::class, Employees::class,User::class,ComposeData::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun chinookDao(): ChinookDao
}