package com.codeshinobi.bajeti.DBs


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.codeshinobi.bajeti.DAOs.ExpenseDAO
import com.codeshinobi.bajeti.Models.ExpenseEntity

@Database(entities = [(ExpenseEntity::class)], version = 1)
abstract class ExpenseDatabase: RoomDatabase() {

    abstract fun productDao(): ExpenseDAO

    companion object {

        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExpenseDatabase::class.java,
                        "expense_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}