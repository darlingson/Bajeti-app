package com.codeshinobi.bajeti.Models
import androidx.room.ColumnInfo
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
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
@Database(entities = [ExpenseEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): ExpenseDAO
}