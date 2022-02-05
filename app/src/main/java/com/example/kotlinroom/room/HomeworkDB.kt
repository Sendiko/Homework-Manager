package com.example.kotlinroom.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Homework::class],
    version = 1
)

abstract class HomeworkDB : RoomDatabase(){

    abstract fun homeworkDao(): HomeworkDao

    companion object{

        @Volatile private var instace : HomeworkDB? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instace ?: synchronized(LOCK){
            instace ?: buildDatabase(context).also{
                instace = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            HomeworkDB::class.java,
            "homework.manager"
        ).build()

    }

}