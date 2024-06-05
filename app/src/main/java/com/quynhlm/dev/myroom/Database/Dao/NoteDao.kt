package com.quynhlm.dev.myroom.Database.Dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.quynhlm.dev.myroom.Models.Note

@Dao
interface NoteDao {
    //DAO được sử dụng để phân tách logic lưu trữ dữ liệu trong một lớp riêng biệt
    @Insert
    suspend fun insertNode(note : Note)

    @Update
    suspend fun updateNode (note: Note)

    @Delete
    suspend fun deleteNode(note: Note)

    @Query("SELECT * FROM notes")
    fun getAllData() : LiveData<List<Note>>
}