package com.quynhlm.dev.myroom.Database.Dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.quynhlm.dev.myroom.Models.Note

@Database(entities = [Note::class], version = 1)
abstract class NodeDatabase : RoomDatabase(){
    abstract fun  getNoteDao() : NoteDao
    companion object{
        @Volatile

        private var instance : NodeDatabase? = null
        fun getInstance(context: Context) : NodeDatabase {
            if(instance == null){
                instance = Room.databaseBuilder(context,NodeDatabase::class.java,"NoteDatabase").build()
            }
            return instance!!
        }
    }
}