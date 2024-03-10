package com.codeshinobi.bajeti.newUI

//class Expenses_App_DB {
//}
import android.content.Context
import androidx.room.*
import androidx.room.Room.databaseBuilder
import com.codeshinobi.bajeti.newUI.DAOs.BudgetDao
import com.codeshinobi.bajeti.newUI.DAOs.ExpenseDao
import com.codeshinobi.bajeti.newUI.DAOs.SpendBudgetDao
import com.codeshinobi.bajeti.newUI.Entities.Budget
import com.codeshinobi.bajeti.newUI.Entities.Expense
import com.codeshinobi.bajeti.newUI.Entities.SpendBudget

@Database(
    entities = [Budget::class, Expense::class, SpendBudget::class],
    version = 1,
    exportSchema = false
)
abstract class Expenses_App_DB : RoomDatabase() {
//    abstract fun budgetDao(): BudgetDao
//    abstract fun expenseDao(): ExpenseDao
//    abstract fun spendBudgetDao(): SpendBudgetDao

    abstract val budgetDao: BudgetDao?
    abstract val expenseDao: ExpenseDao?
    abstract val spendBudgetDao: SpendBudgetDao?

    companion object {
        const val DATABASE_NAME = "expenses_app_db"
        private var INSTANCE: Expenses_App_DB? = null
        fun getDatabase(context: Context): Expenses_App_DB {
            if (INSTANCE == null) {
                INSTANCE = databaseBuilder(
                    context.applicationContext,
                    Expenses_App_DB::class.java,
                    DATABASE_NAME
                ).build()
            }
            return INSTANCE!!
        }
        }
}
