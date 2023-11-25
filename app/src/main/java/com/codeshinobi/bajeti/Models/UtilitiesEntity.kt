package com.codeshinobi.bajeti.Models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UtilitiesEntity(
    @PrimaryKey
    @NonNull
    val id: Int = 0,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "amount")val amount: Int?,
    @ColumnInfo(name = "date")val date: String,
    @ColumnInfo(name = "type")val type: String
)
