package com.example.kotlinroom.room

import androidx.room.*

@Dao
interface HomeworkDao {

    @Insert
    suspend fun addHomework(homework: Homework)

    @Update
    suspend fun updateHomework(homework: Homework)

    @Delete
    suspend fun deleteHomework(homework: Homework)

    @Query("SELECT * FROM homework")
    suspend fun getHomework():List<Homework>

    @Query("SELECT * FROM homework WHERE id =:idols_id")
    suspend fun getHomeworks(idols_id: Int): List<Homework>

    @Query("SELECT * FROM homework ORDER BY mapel ASC")
    suspend fun sortHomework(): List<Homework>

//    @Query("SELECT * FROM idols ORDER BY id =:idols_id ASC")
//    suspend fun sortIdolID(idols_id: Int): List<Idols>
//
//    @Query("SELECT * FROM idols ORDER BY `group` ASC")
//    suspend fun sortIdolG(idols_id: Int): List<Idols>

}