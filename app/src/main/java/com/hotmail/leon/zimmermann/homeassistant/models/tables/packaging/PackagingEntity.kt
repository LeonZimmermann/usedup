package com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packagings")
data class PackagingEntity(var text: String) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "packaging_id")
    var id: Long = 0
}