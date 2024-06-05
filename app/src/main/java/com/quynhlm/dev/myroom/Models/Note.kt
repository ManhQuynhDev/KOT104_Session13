package com.quynhlm.dev.myroom.Models
import androidx.room.ColumnInfo
import  androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "content") var content: String,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0
)