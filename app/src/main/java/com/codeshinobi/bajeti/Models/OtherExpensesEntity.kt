package com.codeshinobi.bajeti.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OtherExpensesEntity(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int =0,
    @ColumnInfo(name = "name")val name: String?,
    @ColumnInfo(name = "amount")val amount: Int?,
)
