package com.codeshinobi.bajeti.Models

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.codeshinobi.bajeti.DAOs.ExpenseDAO


@Entity
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "name")val name: String?,
    @ColumnInfo(name = "type")val type: String?,
    @ColumnInfo(name = "category")val Category: String?,
    @ColumnInfo(name = "amount")val amount: Int?
)
//@Database(entities = [ExpenseEntity::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun ExpenseDAO(): ExpenseDAO
//}
//val db = Room.databaseBuilder(
//    applicationContext,
//    AppDatabase::class.java, "expense.db"
//).build()

@Database(entities = [ExpenseEntity::class], version = 1)
abstract class ExpensesDatabase : RoomDatabase() {
    abstract val ExpenseDAO: ExpenseDAO?

    companion object {
        const val DATABASE_NAME = "expenses_db"
        private var instance: ExpensesDatabase? = null
        fun getInstance(context: Context): ExpensesDatabase? {
            if (instance == null) {
                instance = databaseBuilder(
                    context.applicationContext,
                    ExpensesDatabase::class.java,
                    DATABASE_NAME
                ).build()
            }
            return instance
        }
    }
}