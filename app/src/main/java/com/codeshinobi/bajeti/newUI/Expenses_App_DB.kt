package com.codeshinobi.bajeti.newUI

//class Expenses_App_DB {
//}
import android.content.Context
import androidx.room.*
import com.codeshinobi.bajeti.newUI.DAOs.BudgetDao
import com.codeshinobi.bajeti.newUI.DAOs.ExpenseDao
import com.codeshinobi.bajeti.newUI.DAOs.SpendBudgetDao

@Database(
    entities = [Budget::class, Expense::class, SpendBudget::class],
    version = 1,
    exportSchema = false
)
abstract class Expenses_App_DB : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun spendBudgetDao(): SpendBudgetDao
    companion object {
        @Volatile
        private var INSTANCE: Expenses_App_DB? = null

        fun getDatabase(context: Context): Expenses_App_DB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Expenses_App_DB::class.java,
                    "Expenses_App_DB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}