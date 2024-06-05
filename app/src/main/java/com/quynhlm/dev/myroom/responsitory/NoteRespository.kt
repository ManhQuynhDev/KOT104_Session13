package com.quynhlm.dev.myroom.responsitory

import android.app.Application
import androidx.lifecycle.LiveData
import com.quynhlm.dev.myroom.Database.Dao.NoteDao
import com.quynhlm.dev.myroom.Database.Dao.NodeDatabase
import com.quynhlm.dev.myroom.Models.Note

class NoteRespository (app:Application) {
    private val noteDao : NoteDao

    init {
        val noteDatabase : NodeDatabase = NodeDatabase.getInstance(app)
        noteDao = noteDatabase.getNoteDao()
    }

    suspend fun insertNote(note: Note) = noteDao.insertNode(note)
    suspend fun updateNote(note: Note) = noteDao.updateNode(note)
    suspend fun deleteNote(note: Note) = noteDao.deleteNode(note)

    fun getAllNote() : LiveData<List<Note>> = noteDao.getAllData()
}