package com.hotmail.leon.zimmermann.homeassistant.models.tables.packaging

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "packagings")
data class PackagingEntity(
    var text: String
) {
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}