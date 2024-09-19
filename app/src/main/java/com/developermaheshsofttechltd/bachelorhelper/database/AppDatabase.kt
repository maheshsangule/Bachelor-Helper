package com.developermaheshsofttechltd.bachelorhelper.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.developermaheshsofttechltd.bachelorhelper.daos.BookmarksDao
import com.developermaheshsofttechltd.bachelorhelper.daos.NotesDao
import com.developermaheshsofttechltd.bachelorhelper.entities.BookmarksEntity
import com.developermaheshsofttechltd.bachelorhelper.entities.NotesEntity

@Database(
    entities = [BookmarksEntity::class, NotesEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun bookmarksDao(): BookmarksDao
    abstract fun notesDao(): NotesDao


    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "books_db"
                    ).build()
                }
            }
            return INSTANCE
        }


    }


}



















