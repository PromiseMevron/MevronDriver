package com.mevron.rides.driver.localdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "saved_address")
data class SavedAddress(
    var type: String,
    var name: String,
    var lat: String,
    var lng: String,
    var address: String,
    var uiid: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
